package com.Rede_Social.Service.Email;

import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.StatusEmail;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Email.EmailSendException;
import com.Rede_Social.Repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JavaMailSender mailSender;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new UserEntity();
        mockUser.setUuid(UUID.randomUUID());
        mockUser.setNome("John Doe");
        mockUser.setEmail("johndoe@example.com");
    }

    @Test
    void testCriarEmailComDadosValidos() {
        EmailEntity emailEntity = emailService.criarEmail(mockUser);
        assertEquals(mockUser.getUuid(), emailEntity.getOwnerRef());
        assertEquals("johndoe@example.com", emailEntity.getEmailTo());
        assertEquals("Bem-vindo(a) ao Rede Social! 🎉", emailEntity.getSubject());
        assertTrue(emailEntity.getText().contains(mockUser.getNome()));
        assertTrue(emailEntity.getText().contains("http://localhost:8080/api/user/validar-conta"));
    }

    @Test
    void testCriarEmailComNomeESemEmail() {
        mockUser.setEmail("");
        EmailEntity emailEntity = emailService.criarEmail(mockUser);
        assertEquals(mockUser.getUuid(), emailEntity.getOwnerRef());
        assertEquals("", emailEntity.getEmailTo());
        assertEquals("Bem-vindo(a) ao Rede Social! 🎉", emailEntity.getSubject());
        assertTrue(emailEntity.getText().contains(mockUser.getNome()));
        assertTrue(emailEntity.getText().contains("http://localhost:8080/api/user/validar-conta"));
    }

    @Test
    void testEnviarEmailComSucesso() {
        EmailEntity emailEntity = emailService.criarEmail(mockUser);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        emailService.enviaEmail(emailEntity);
        assertEquals(StatusEmail.SENT, emailEntity.getStatusEmail());
        assertNotNull(emailEntity.getSendDateEmail());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(emailEntity);
    }

    @Test
    void testEnviarEmailComSucessoMultiploDestinatario() {
        mockUser.setEmail("johndoe@example.com, anotheremail@example.com");
        EmailEntity emailEntity = emailService.criarEmail(mockUser);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        emailService.enviaEmail(emailEntity);
        assertEquals(StatusEmail.SENT, emailEntity.getStatusEmail());
        assertNotNull(emailEntity.getSendDateEmail());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(emailEntity);
    }

    @Test
    void testEnviarEmailComErro() {
        EmailEntity emailEntity = emailService.criarEmail(mockUser);
        doThrow(new MailException("Erro ao enviar email") {
        }).when(mailSender).send(any(SimpleMailMessage.class));
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            emailService.enviaEmail(emailEntity);
        });
        assertEquals("Erro ao enviar o e-mail para: johndoe@example.com", exception.getMessage());
        assertEquals(StatusEmail.ERROR, emailEntity.getStatusEmail());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(emailEntity);
    }

    @Test
    void testEnviarEmailComErroSemDestinatario() {
        mockUser.setEmail("");
        EmailEntity emailEntity = emailService.criarEmail(mockUser);
        doThrow(new MailException("Erro ao enviar email") {
        }).when(mailSender).send(any(SimpleMailMessage.class));
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            emailService.enviaEmail(emailEntity);
        });

        assertEquals("Erro ao enviar o e-mail para: ", exception.getMessage());
        assertEquals(StatusEmail.ERROR, emailEntity.getStatusEmail());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(emailEntity);
    }

    @Test
    void testGenerateHashComDadosValidos() {
        String hash = EmailService.generateHash(mockUser.getNome(), mockUser.getEmail());
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 gera 64 caracteres em hexadecimal
    }

    @Test
    void testGenerateHashComDadosVazios() {
        String hash = EmailService.generateHash("", "");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }
}

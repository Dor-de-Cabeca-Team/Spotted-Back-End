package com.Rede_Social.Service;

import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    UserService userService;

    UserEntity user;
    UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        user = new UserEntity();
        user.setUuid(uuid);
        user.setNome("Test User");
        user.setEmail("test@example.com");
    }

    @Test
    void testSave_Success() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(emailService.criarEmail(any(UserEntity.class))).thenReturn(new EmailEntity());

        UserEntity savedUser = userService.save(user);

        assertNotNull(savedUser);
        verify(userRepository).save(user);
        verify(emailService).criarEmail(user);
        verify(emailService).enviaEmail(any(EmailEntity.class));
    }

    @Test
    void testSave_EmptyEmail() {
        user.setEmail("");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity savedUser = userService.save(user);

        assertNotNull(savedUser);
        verify(userRepository).save(user);
        verify(emailService, never()).criarEmail(any(UserEntity.class));
        verify(emailService, never()).enviaEmail(any(EmailEntity.class));
    }

    @Test
    void testSave_Exception() {
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("Test exception"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.save(user));
        assertTrue(exception.getMessage().contains("Erro no service, n deu para salvar o usuario no repository"));
    }

    @Test
    void testUpdate_Success() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity updatedUser = userService.update(user, uuid);

        assertNotNull(updatedUser);
        verify(userRepository).findById(uuid);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdate_UserNotFound() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(user, uuid));
    }

    @Test
    void testUpdate_Exception() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("Test exception"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.update(user, uuid));
        assertTrue(exception.getMessage().contains("Erro no service, n deu para atualizar o usuario no repository"));
    }

    @Test
    void testDelete_Success() {
        String result = userService.delete(uuid);

        assertEquals("usuario deletado", result);
        verify(userRepository).deleteById(uuid);
    }

    @Test
    void testDelete_Exception() {
        doThrow(new RuntimeException("Test exception")).when(userRepository).deleteById(uuid);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.delete(uuid));
        assertTrue(exception.getMessage().contains("Erro no service, n deu para deletar o usuario no repository"));
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findById(uuid);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(uuid));
    }

    @Test
    void testFindAll_Success() {
        List<UserEntity> userList = Arrays.asList(user, new UserEntity());
        when(userRepository.findAll()).thenReturn(userList);

        List<UserEntity> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindAll_Exception() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("Test exception"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.findAll());
        assertTrue(exception.getMessage().contains("Erro no service, n deu para listar todos users"));
    }

    @Test
    void testValidarConta_Success() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        String hashGerado = EmailService.generateHash(user.getNome(), user.getEmail());
        boolean result = userService.validarConta(uuid, hashGerado);

        assertTrue(result);
        assertTrue(user.getAtivo());
        verify(userRepository).save(user);
    }

    @Test
    void testValidarConta_InvalidHash() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        boolean result = userService.validarConta(uuid, "invalidHash");

        assertFalse(result);
        assertFalse(user.getAtivo());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testValidarConta_UserNotFound() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.validarConta(uuid, "anyHash"));
    }
}
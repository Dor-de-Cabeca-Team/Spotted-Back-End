package com.Rede_Social.Controller;

import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;
    @MockBean
    UserRepository userRepository;
    @MockBean
    EmailService emailService;

    @Test
    void saveSuccess() {
        UserEntity user = new UserEntity();
        String email = "test@example.com";
        user.setEmail(email);

        when(userRepository.save(user)).thenReturn(user);
        when(emailService.criarEmail(user)).thenReturn(new EmailEntity());
        doNothing().when(emailService).enviaEmail(any(EmailEntity.class));

        ResponseEntity<UserEntity> response = userController.save(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void saveFailure() {
        UserEntity user = new UserEntity();
        when(userRepository.save(user)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<UserEntity> response = userController.save(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSuccess() {
        UUID uuid = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setUuid(uuid);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<UserEntity> response = userController.update(user, uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void updateFailure() {
        UUID uuid = UUID.randomUUID();
        UserEntity user = new UserEntity();
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenThrow(new RuntimeException());

        ResponseEntity<UserEntity> response = userController.update(user, uuid);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(uuid);

        ResponseEntity<String> response = userController.delete(uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("usuario deletado", response.getBody());
    }

    @Test
    void deleteFailure() {
        UUID uuid = UUID.randomUUID();
        doThrow(new RuntimeException()).when(userRepository).deleteById(uuid);

        ResponseEntity<String> response = userController.delete(uuid);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findByIdSuccess() {
        UUID uuid = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setUuid(uuid);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        ResponseEntity<UserEntity> response = userController.findById(uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void findByIdFailure() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenThrow(new RuntimeException());

        ResponseEntity<UserEntity> response = userController.findById(uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllSuccess() {
        UserEntity user = new UserEntity();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<UserEntity>> response = userController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findAllFailure() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<UserEntity>> response = userController.findAll();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void validarContaSuccess() {
        UUID idUser = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setNome("Test");
        user.setEmail("test@example.com");
        String hash = "hash";

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        try (MockedStatic<EmailService> mocked = Mockito.mockStatic(EmailService.class)) {
            mocked.when(() -> EmailService.generateHash(user.getNome(), user.getEmail())).thenReturn(hash);

            ResponseEntity<String> response = userController.validarConta(idUser, hash);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Conta validada com sucesso!", response.getBody());
        }
    }

    @Test
    void validarContaFailure() {
        UUID idUser = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setNome("Test");
        user.setEmail("test@example.com");
        String hash = "hash";
        String falseHash = "false";

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        try (MockedStatic<EmailService> mocked = Mockito.mockStatic(EmailService.class)) {
            mocked.when(() -> EmailService.generateHash(user.getNome(), user.getEmail())).thenReturn(hash);

            ResponseEntity<String> response = userController.validarConta(idUser, falseHash);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Falha na validação da conta.", response.getBody());
        }
    }
}
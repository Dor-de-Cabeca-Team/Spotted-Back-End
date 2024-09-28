package com.Rede_Social.Controller;

import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Repository.UserRepository;
import org.junit.jupiter.api.Test;
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

    @Test
    void saveSuccess() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        when(userRepository.save(user)).thenReturn(user);

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

//    @Test
//    void validarContaSuccess() {
//        UUID idUser = UUID.randomUUID();
//        String hash = "someHash";
//        UserEntity user = new UserEntity();
//        user.setNome("Test User");
//        user.setEmail("test@example.com");
//
//        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
//        when(EmailService.generateHash(user.getNome(), user.getEmail())).thenReturn(hash);
//
//        ResponseEntity<String> response = userController.validarConta(idUser, hash);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Conta validada com sucesso!", response.getBody());
//    }
}
package com.Rede_Social.Controller;

import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class LikeControllerTest {
    @Autowired
    LikeController likeController;
    @MockBean
    LikeRepository likeRepository;

    @Test
    void saveSuccess() {
        LikeEntity like = new LikeEntity();
        like.setUuid(UUID.randomUUID());

        when(likeRepository.save(any(LikeEntity.class))).thenReturn(like);

        ResponseEntity<LikeEntity> response = likeController.save(like);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(like.getUuid(), response.getBody().getUuid());
    }

    @Test
    void saveFailure() {
        LikeEntity like = new LikeEntity();

        when(likeRepository.save(any(LikeEntity.class))).thenThrow(new RuntimeException("Erro ao salvar o like"));

        ResponseEntity<LikeEntity> response = likeController.save(like);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSuccess() {
        UUID likeId = UUID.randomUUID();
        LikeEntity like = new LikeEntity();
        like.setUuid(likeId);

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));
        when(likeRepository.save(like)).thenReturn(like);

        ResponseEntity<LikeEntity> response = likeController.update(like, likeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(like.getUuid(), response.getBody().getUuid());
    }

    @Test
    void updateFailure(){
        UUID likeId = UUID.randomUUID();
        LikeEntity like = new LikeEntity();
        like.setUuid(likeId);

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));
        when(likeRepository.save(like)).thenThrow(new RuntimeException());

        ResponseEntity<LikeEntity> response = likeController.update(like, likeId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID likeId = UUID.randomUUID();

        doNothing().when(likeRepository).deleteById(likeId);

        ResponseEntity<String> response = likeController.delete(likeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Like deletado", response.getBody());
    }

    @Test
    void deleteFailure(){
        UUID likeId = UUID.randomUUID();

        doThrow(new RuntimeException()).when(likeRepository).deleteById(likeId);

        ResponseEntity<String> response = likeController.delete(likeId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findByIdSuccess() {
        UUID likeId = UUID.randomUUID();
        LikeEntity like = new LikeEntity();
        like.setUuid(likeId);

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));

        ResponseEntity<LikeEntity> response = likeController.findById(likeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(likeId, response.getBody().getUuid());
    }

    @Test
    void findByIdFailure(){
        UUID likeId = UUID.randomUUID();

        when(likeRepository.findById(likeId)).thenReturn(Optional.empty());

        ResponseEntity<LikeEntity> response = likeController.findById(likeId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllSuccess() {
        List<LikeEntity> likes = List.of(new LikeEntity(), new LikeEntity());

        when(likeRepository.findAll()).thenReturn(likes);

        ResponseEntity<List<LikeEntity>> response = likeController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(likes.size(), response.getBody().size());
    }

    @Test
    void findAllFailure(){
        when(likeRepository.findAll()).thenThrow(new RuntimeException("Erro ao listar os likes"));

        ResponseEntity<List<LikeEntity>> response = likeController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
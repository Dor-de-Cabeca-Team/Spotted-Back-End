package com.Rede_Social.Controller;

import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.TagRepository;
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
class TagControllerTest {
    @Autowired
    TagController tagController;

    @MockBean
    TagRepository tagRepository;

    @Test
    void saveSuccess() {
        TagEntity tag = new TagEntity();
        tag.setUuid(UUID.randomUUID());
        when(tagRepository.save(tag)).thenReturn(tag);
        ResponseEntity<TagEntity> response = tagController.save(tag);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
    }

    @Test
    void saveFailure() {
        TagEntity tag = new TagEntity();
        when(tagRepository.save(tag)).thenThrow(new RuntimeException("Erro ao salvar"));

        ResponseEntity<TagEntity> response = tagController.save(tag);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSuccess() {
        TagEntity tag = new TagEntity();
        UUID uuid = UUID.randomUUID();
        tag.setUuid(uuid);
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);

        ResponseEntity<TagEntity> response = tagController.update(tag, uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
    }

    @Test
    void updateFailure() {
        TagEntity tag = new TagEntity();
        UUID uuid = UUID.randomUUID();
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenThrow(new RuntimeException());

        ResponseEntity<TagEntity> response = tagController.update(tag, uuid);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(tagRepository).deleteById(uuid);

        ResponseEntity<String> response = tagController.delete(uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tag deletada", response.getBody());
    }

    @Test
    void deleteFailure() {
        UUID uuid = UUID.randomUUID();
        doThrow(new RuntimeException()).when(tagRepository).deleteById(uuid);

        ResponseEntity<String> response = tagController.delete(uuid);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findByIdSuccess() {
        UUID uuid = UUID.randomUUID();
        TagEntity tag = new TagEntity();
        tag.setUuid(uuid);
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(tag));

        ResponseEntity<TagEntity> response = tagController.findById(uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
    }

    @Test
    void findByIdFailure() {
        UUID uuid = UUID.randomUUID();
        when(tagRepository.findById(uuid)).thenThrow(new RuntimeException());

        ResponseEntity<TagEntity> response = tagController.findById(uuid);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllSuccess() {
        TagEntity tag = new TagEntity();
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));

        ResponseEntity<List<TagEntity>> response = tagController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findAllFailure() {
        when(tagRepository.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<TagEntity>> response = tagController.findAll();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

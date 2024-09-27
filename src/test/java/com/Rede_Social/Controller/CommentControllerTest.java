package com.Rede_Social.Controller;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentControllerTest {

    @Autowired
    CommentController commentController;
    @MockBean
    CommentService commentService;

    @Test
    void saveSuccess() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String conteudo = "Este é um comentário de teste.";
        CommentEntity comment = new CommentEntity();
        comment.setUuid(UUID.randomUUID());
        comment.setConteudo(conteudo);
        comment.setValido(true);
        comment.setData(Instant.now());

        when(commentService.save(postId, userId, comment)).thenReturn(comment);

        ResponseEntity<CommentEntity> response = commentController.save(postId, userId, comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conteudo, response.getBody().getConteudo());
        assertTrue(response.getBody().isValido());
    }

    @Test
    void saveFailure() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CommentEntity comment = new CommentEntity();
        String conteudo = "Comentário teste";
        comment.setConteudo(conteudo);

        when(commentService.save(postId, userId, comment)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> retorno = commentController.save(postId, userId, comment);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
    }

    @Test
    void updateSuccess() {
        UUID commentId = UUID.randomUUID();
        String content = "Comentário atualizado";

        CommentEntity comment = new CommentEntity();
        comment.setUuid(commentId);
        comment.setConteudo(content);
        comment.setValido(true);
        comment.setData(Instant.now());

        when(commentService.update(comment, commentId)).thenReturn(comment);

        ResponseEntity<CommentEntity> response = commentController.update(comment, commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(content, response.getBody().getConteudo());
        assertTrue(response.getBody().isValido());
    }

    @Test
    void updateFailure() {
        UUID commentId = UUID.randomUUID();
        String content = "Comentário atualizado";

        CommentEntity comment = new CommentEntity();
        comment.setUuid(commentId);
        comment.setConteudo(content);
        comment.setValido(true);
        comment.setData(Instant.now());

        when(commentService.update(comment, commentId)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> response = commentController.update(comment, commentId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID commentId = UUID.randomUUID();
        String expectedResponse = "Comentário deletado com sucesso";

        when(commentService.delete(commentId)).thenReturn(expectedResponse);

        ResponseEntity<String> response = commentController.delete(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void deleteFailure() {
        UUID commentId = UUID.randomUUID();

        when(commentService.delete(commentId)).thenThrow(new RuntimeException());

        ResponseEntity<String> response = commentController.delete(commentId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findByIdSuccess() {
        UUID commentId = UUID.randomUUID();
        String content = "Comentário encontrado";
        CommentEntity comment = new CommentEntity();
        comment.setUuid(commentId);
        comment.setConteudo(content);
        comment.setValido(true);
        comment.setData(Instant.now());

        when(commentService.findById(commentId)).thenReturn(comment);

        ResponseEntity<CommentEntity> response = commentController.findById(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(content, response.getBody().getConteudo());
        assertTrue(response.getBody().isValido());
    }

    @Test
    void findByIdFailure() {
        UUID commentId = UUID.randomUUID();

        when(commentService.findById(commentId)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> response = commentController.findById(commentId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllSuccess() {
        CommentEntity comment1 = new CommentEntity();
        comment1.setUuid(UUID.randomUUID());
        comment1.setConteudo("Comentário 1");
        comment1.setValido(true);
        comment1.setData(Instant.now());

        CommentEntity comment2 = new CommentEntity();
        comment2.setUuid(UUID.randomUUID());
        comment2.setConteudo("Comentário 2");
        comment2.setValido(true);
        comment2.setData(Instant.now());

        List<CommentEntity> comments = Arrays.asList(comment1, comment2);

        when(commentService.findAll()).thenReturn(comments);

        ResponseEntity<List<CommentEntity>> response = commentController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void findAllFailure() {
        when(commentService.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<CommentEntity>> response = commentController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

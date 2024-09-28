package com.Rede_Social.Controller;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Repository.CommentRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.AI.GeminiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CommentControllerTest {
    @Autowired
    CommentController commentController;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    PostRepository postRepository;
    @MockBean
    GeminiService geminiService;

    @Test
    void saveSuccess() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String conteudo = "Comentário teste";

        UserEntity user = new UserEntity();
        user.setUuid(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        PostEntity post = new PostEntity();
        post.setUuid(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        when(geminiService.validadeAI(conteudo)).thenReturn(true);

        CommentEntity savedComment = new CommentEntity();
        savedComment.setUuid(UUID.randomUUID());
        savedComment.setData(Instant.now());
        savedComment.setConteudo(conteudo);
        savedComment.setValido(true);
        savedComment.setUser(user);
        savedComment.setPost(post);

        when(commentRepository.save(any(CommentEntity.class))).thenReturn(savedComment);

        ResponseEntity<CommentEntity> response = commentController.save(postId, userId, conteudo);

        assertNotNull(response, "A resposta não deve ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conteudo, response.getBody().getConteudo());
        assertTrue(response.getBody().isValido());
        assertEquals(userId, response.getBody().getUser().getUuid());
        assertEquals(postId, response.getBody().getPost().getUuid());
    }

    @Test
    void savePostNotFound() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String conteudo = "Comentário teste";

        UserEntity user = new UserEntity();
        user.setUuid(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(postRepository.findById(postId)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> response = commentController.save(postId, userId, conteudo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void saveUserNotFound() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String conteudo = "Comentário teste";

        UserEntity user = new UserEntity();
        user.setUuid(userId);
        when(userRepository.findById(userId)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> response = commentController.save(postId, userId, conteudo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void saveFailure() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CommentEntity comment = new CommentEntity();
        String conteudo = "Comentário teste";
        comment.setConteudo(conteudo);

        when(commentRepository.save(comment)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> retorno = commentController.save(postId, userId, conteudo);

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

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

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

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenThrow(new RuntimeException());

        ResponseEntity<CommentEntity> response = commentController.update(comment, commentId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID commentId = UUID.randomUUID();

        doNothing().when(commentRepository).deleteById(commentId);

        ResponseEntity<String> response = commentController.delete(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comentário deletado", response.getBody());
    }

    @Test
    void deleteFailure() {
        UUID commentId = UUID.randomUUID();

        Mockito.doThrow(new RuntimeException()).when(commentRepository).deleteById(commentId);

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

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        ResponseEntity<CommentEntity> response = commentController.findById(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(content, response.getBody().getConteudo());
        assertTrue(response.getBody().isValido());
    }

    @Test
    void findByIdFailure() {
        UUID commentId = UUID.randomUUID();

        when(commentRepository.findById(commentId)).thenThrow(new RuntimeException());

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

        when(commentRepository.findAll()).thenReturn(comments);

        ResponseEntity<List<CommentEntity>> response = commentController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void findAllFailure() {
        when(commentRepository.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<CommentEntity>> response = commentController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

package com.Rede_Social.Service;

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

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PostRepository postRepository;

    @MockBean
    GeminiService geminiService;

    @Test
    void testSaveComment() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String commentText = "Great post!";

        UserEntity mockUser = new UserEntity();
        PostEntity mockPost = new PostEntity();
        CommentEntity mockComment = new CommentEntity(UUID.randomUUID(), Instant.now(), commentText, true, null, null, mockUser, mockPost);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        Mockito.when(geminiService.validadeAI(commentText)).thenReturn(true);
        Mockito.when(commentRepository.save(any(CommentEntity.class))).thenReturn(mockComment);

        CommentEntity savedComment = commentService.save(postId, userId, commentText);

        assertNotNull(savedComment);
        assertEquals(commentText, savedComment.getConteudo());
        assertTrue(savedComment.isValido());
    }
    @Test
    void testSaveCommentInvalidByAI() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String commentText = "Comentário ofensivo!";
        PostEntity post = new PostEntity();
        UserEntity user = new UserEntity();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(geminiService.validadeAI(commentText)).thenReturn(false);
        Mockito.when(commentRepository.save(Mockito.any(CommentEntity.class))).thenReturn(new CommentEntity());

        CommentEntity result = commentService.save(postId, userId, commentText);

        assertFalse(result.isValido());
    }



    @Test
    void testUpdateComment() {
        UUID commentId = UUID.randomUUID();
        CommentEntity existingComment = new CommentEntity();
        existingComment.setUuid(commentId);
        existingComment.setConteudo("Old content");

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        Mockito.when(commentRepository.save(any(CommentEntity.class))).thenReturn(existingComment);

        CommentEntity updatedComment = commentService.update(existingComment, commentId);
        assertEquals("Old content", updatedComment.getConteudo());
        assertEquals(commentId, updatedComment.getUuid());
    }

    @Test
    void testUpdateCommentNotFound() {
        UUID commentId = UUID.randomUUID();
        CommentEntity newComment = new CommentEntity();

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            commentService.update(newComment, commentId);
        });
    }

    @Test
    void testDeleteComment() {
        UUID commentId = UUID.randomUUID();
        Mockito.doNothing().when(commentRepository).deleteById(commentId);

        String result = commentService.delete(commentId);
        assertEquals("Comentário deletado", result);
    }

    @Test
    void testDeleteCommentException() {
        UUID commentId = UUID.randomUUID();
        Mockito.doThrow(new RuntimeException("Erro ao deletar")).when(commentRepository).deleteById(commentId);

        assertThrows(RuntimeException.class, () -> {
            commentService.delete(commentId);
        });
    }

    @Test
    void testFindCommentById() {
        UUID commentId = UUID.randomUUID();
        CommentEntity mockComment = new CommentEntity();
        mockComment.setUuid(commentId);

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        CommentEntity foundComment = commentService.findById(commentId);
        assertNotNull(foundComment);
        assertEquals(commentId, foundComment.getUuid());
    }

    @Test
    void testFindCommentByIdNotFound() {
        UUID commentId = UUID.randomUUID();

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            commentService.findById(commentId);
        });
    }

    @Test
    void testFindAllComments() {
        List<CommentEntity> mockComments = List.of(new CommentEntity(), new CommentEntity());

        Mockito.when(commentRepository.findAll()).thenReturn(mockComments);

        List<CommentEntity> comments = commentService.findAll();
        assertEquals(2, comments.size());
    }

    @Test
    void testFindAllCommentsException() {
        Mockito.when(commentRepository.findAll()).thenThrow(new RuntimeException("Erro ao listar"));

        assertThrows(RuntimeException.class, () -> {
            commentService.findAll();
        });
    }


}

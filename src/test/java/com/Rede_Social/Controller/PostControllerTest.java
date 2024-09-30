package com.Rede_Social.Controller;

import com.Rede_Social.Entity.*;
import com.Rede_Social.Repository.*;
import com.Rede_Social.Service.AI.GeminiService;
import com.Rede_Social.Service.PostService;
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
import static org.mockito.Mockito.*;

@SpringBootTest
class PostControllerTest {
    @Autowired
    PostController postController;

    @MockBean
    PostRepository postRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TagRepository tagRepository;

    @MockBean
    LikeRepository likeRepository;

    @MockBean
    ComplaintRepository complaintRepository;

    @MockBean
    CommentRepository commentRepository;


    @Test
    void saveSuccess() {
        UUID userId = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setUuid(userId);
        user.setNome("Test User");
        UUID tagId = UUID.randomUUID();
        TagEntity tag = new TagEntity();
        tag.setUuid(tagId);
        tag.setNome("Test Tag");
        PostEntity post = new PostEntity();
        post.setUser(user);
        post.setConteudo("Conteúdo de Teste");
        post.setTags(List.of(tag));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tagRepository.findAllById(anyList())).thenReturn(List.of(tag));
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        ResponseEntity<PostEntity> response = postController.save(post);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());
    }

    @Test
    void saveFailure() {
        PostEntity post = new PostEntity();

        when(postRepository.save(post)).thenThrow(new RuntimeException());

        ResponseEntity<PostEntity> response = postController.save(post);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSuccess() {
        PostEntity post = new PostEntity();
        UUID uuid = UUID.randomUUID();
        post.setUuid(uuid);

        when(postRepository.findById(uuid)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        ResponseEntity<PostEntity> response = postController.update(post, uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());
    }

    @Test
    void updateFailure() {
        PostEntity post = new PostEntity();
        UUID uuid = UUID.randomUUID();

        when(postRepository.findById(uuid)).thenReturn(Optional.empty());

        ResponseEntity<PostEntity> response = postController.update(post, uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID uuid = UUID.randomUUID();

        doNothing().when(postRepository).deleteById(uuid);

        ResponseEntity<String> response = postController.delete(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post deletado", response.getBody());
    }

    @Test
    void deleteFailure() {
        UUID uuid = UUID.randomUUID();

        doThrow(new RuntimeException()).when(postRepository).deleteById(uuid);

        ResponseEntity<String> response = postController.delete(uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findByIdSuccess() {
        UUID uuid = UUID.randomUUID();
        PostEntity post = new PostEntity();
        post.setUuid(uuid);

        when(postRepository.findById(any(UUID.class))).thenReturn(Optional.of(post));

        ResponseEntity<PostEntity> response = postController.findById(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());
    }

    @Test
    void findByIdFailure() {
        UUID uuid = UUID.randomUUID();

        when(postRepository.findById(uuid)).thenReturn(Optional.empty());

        ResponseEntity<PostEntity> response = postController.findById(uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllSuccess() {
        PostEntity post1 = new PostEntity();
        PostEntity post2 = new PostEntity();
        List<PostEntity> posts = List.of(post1, post2);

        when(postRepository.findAll()).thenReturn(posts);

        ResponseEntity<List<PostEntity>> response = postController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }

    @Test
    void findAllFailure() {
        when(postRepository.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<PostEntity>> response = postController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void darLikePostSuccess() {
        UUID idPost = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        String responseMessage = "Like no Post dado";

        when(postRepository.findById(idPost)).thenReturn(Optional.of(new PostEntity()));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(new UserEntity()));
        when(likeRepository.save(any(LikeEntity.class))).thenReturn(new LikeEntity());

        ResponseEntity<String> response = postController.darLikePost(idPost, idUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }


    @Test
    void darLikePostFailure() {
        UUID idPost = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();

        when(postRepository.findById(idPost)).thenReturn(Optional.of(new PostEntity()));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(new UserEntity()));
        when(likeRepository.save(any(LikeEntity.class))).thenThrow(new RuntimeException());

        ResponseEntity<String> response = postController.darLikePost(idPost, idUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void darLikeComentarioSuccess() {
        UUID idComentario = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        String responseMessage = "Like no comentario dado";

        when(commentRepository.findById(idComentario)).thenReturn(Optional.of(new CommentEntity()));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(new UserEntity()));
        when(likeRepository.save(any(LikeEntity.class))).thenReturn(new LikeEntity());

        ResponseEntity<String> response = postController.darLikeComentario(idComentario, idUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }

    @Test
    void darLikeComentarioFailure() {
        UUID idComentario = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();

        when(commentRepository.findById(idComentario)).thenReturn(Optional.empty());

        ResponseEntity<String> response = postController.darLikeComentario(idComentario, idUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void denunciarPostSuccess() {
        UUID idPost = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setUuid(idUser);
        PostEntity post = new PostEntity();
        post.setUuid(idPost);
        ComplaintEntity complaint = new ComplaintEntity();
        String responseMessage = "Denuncia ao post feita";

        when(postRepository.findById(idPost)).thenReturn(Optional.of(post));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(complaintRepository.save(complaint)).thenReturn(complaint);

        ResponseEntity<String> response = postController.denunciarPost(idPost, idUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }

    @Test
    void denunciarPostFailure() {
        UUID idPost = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setUuid(idUser);
        PostEntity post = new PostEntity();
        post.setUuid(idPost);

        when(postRepository.findById(idPost)).thenReturn(Optional.of(post));
        when(userRepository.findById(idUser)).thenThrow(new RuntimeException());

        ResponseEntity<String> response = postController.denunciarPost(idPost, idUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void denunciarComentarioSuccess() {
        UUID idComment = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        CommentEntity comment = new CommentEntity();
        comment.setUuid(idComment);
        UserEntity user = new UserEntity();
        user.setUuid(idUser);
        ComplaintEntity complaint = new ComplaintEntity();
        String responseMessage = "Denuncia ao comentario feita";

        when(commentRepository.findById(idComment)).thenReturn(Optional.of(comment));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(complaintRepository.save(complaint)).thenReturn(complaint);

        ResponseEntity<String> response = postController.denunciarComentario(idComment, idUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }

    @Test
    void denunciarComentarioFailure() {
        UUID idComment = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        CommentEntity comment = new CommentEntity();
        comment.setUuid(idComment);

        when(commentRepository.findById(idComment)).thenThrow(new RuntimeException());

        ResponseEntity<String> response = postController.denunciarComentario(idComment, idUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

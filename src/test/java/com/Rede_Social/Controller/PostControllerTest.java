package com.Rede_Social.Controller;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Repository.*;
import com.Rede_Social.Service.AI.GeminiService;
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
    GeminiService geminiService;

    @MockBean
    LikeRepository likeRepository;

    @MockBean
    ComplaintRepository complaintRepository;

    @MockBean
    CommentRepository commentRepository;


//    @Test
//    void saveSuccess() {
//        UserEntity user = new UserEntity();
//        user.setUuid(UUID.randomUUID());
//
//        PostEntity post = new PostEntity();
//        post.setUuid(UUID.randomUUID());
//        post.setUser(user);
//
//        when(tagRepository.findAllById()).thenReturn(tags);
//        when(postRepository.save(post)).thenReturn(post);
//
//        ResponseEntity<PostEntity> response = postController.save(post);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(post, response.getBody());
//    }

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

//    @Test
//    void darLikeComentarioFailure() {
//        UUID idComentario = UUID.randomUUID();
//        UUID idUser = UUID.randomUUID();
//
//        // Mock para simular uma exceção no repositório de Likes
//        when(likeRepository.darLikeComentario(idComentario, idUser)).thenThrow(new RuntimeException());
//
//        ResponseEntity<String> response = postController.darLikeComentario(idComentario, idUser);
//
//        assertEquals(400, response.getStatusCodeValue());
//    }
//
//    @Test
//    void denunciarPostSuccess() {
//        UUID idPost = UUID.randomUUID();
//        UUID idUser = UUID.randomUUID();
//        String responseMessage = "Denuncia ao post feita";
//
//        // Mock para o método do repositório de Denúncias
//        when(complaintRepository.denunciarPost(idPost, idUser)).thenReturn(responseMessage);
//
//        ResponseEntity<String> response = postController.denunciarPost(idPost, idUser);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(responseMessage, response.getBody());
//    }
//
//    @Test
//    void denunciarPostFailure() {
//        UUID idPost = UUID.randomUUID();
//        UUID idUser = UUID.randomUUID();
//
//        // Mock para simular uma exceção no repositório de Denúncias
//        when(complaintRepository.denunciarPost(idPost, idUser)).thenThrow(new RuntimeException());
//
//        ResponseEntity<String> response = postController.denunciarPost(idPost, idUser);
//
//        assertEquals(400, response.getStatusCodeValue());
//    }
//
//    @Test
//    void denunciarComentarioSuccess() {
//        UUID idComentario = UUID.randomUUID();
//        UUID idUser = UUID.randomUUID();
//        String responseMessage = "Denuncia ao comentario feita";
//
//        // Mock para o método do repositório de Denúncias
//        when(complaintRepository.denunciarComentario(idComentario, idUser)).thenReturn(responseMessage);
//
//        ResponseEntity<String> response = postController.denunciarComentario(idComentario, idUser);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(responseMessage, response.getBody());
//    }
//
//    @Test
//    void denunciarComentarioFailure() {
//        UUID idComentario = UUID.randomUUID();
//        UUID idUser = UUID.randomUUID();
//
//        // Mock para simular uma exceção no repositório de Denúncias
//        when(complaintRepository.denunciarComentario(idComentario, idUser)).thenThrow(new RuntimeException());
//
//        ResponseEntity<String> response = postController.denunciarComentario(idComentario, idUser);
//
//        assertEquals(400, response.getStatusCodeValue());
//    }
}

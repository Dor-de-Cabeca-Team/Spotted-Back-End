package com.Rede_Social.Service;

import com.Rede_Social.Entity.*;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.*;
import com.Rede_Social.Service.AI.GeminiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

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

    UUID uuid;
    PostEntity post;
    UserEntity user;
    TagEntity tag;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        user = new UserEntity();
        user.setUuid(uuid);
        tag = new TagEntity();
        tag.setUuid(uuid);
        post = new PostEntity();
        post.setUuid(uuid);
        post.setUser(user);
        post.setTags(Collections.singletonList(tag));
        post.setConteudo("Test content");
    }

    @Test
    void testSave_Success() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(tagRepository.findAllById(anyList())).thenReturn(Collections.singletonList(tag));
        when(geminiService.validadeAI(anyString())).thenReturn(true);
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        PostEntity savedPost = postService.save(post);

        assertNotNull(savedPost);
        assertEquals(uuid, savedPost.getUuid());
        verify(postRepository, times(1)).save(any(PostEntity.class));
    }

    @Test
    void testSave_UserNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> postService.save(post));
    }

    @Test
    void testUpdate_Success() {
        when(postRepository.findById(uuid)).thenReturn(Optional.of(post));
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        PostEntity updatedPost = postService.update(post, uuid);

        assertNotNull(updatedPost);
        assertEquals(uuid, updatedPost.getUuid());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUpdate_PostNotFound() {
        when(postRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.update(post, uuid));
    }

    @Test
    void testDelete_Success() {
        doNothing().when(postRepository).deleteById(uuid);

        String result = postService.delete(uuid);

        assertEquals("Post deletado", result);
        verify(postRepository, times(1)).deleteById(uuid);
    }

    @Test
    void testFindById_Success() {
        when(postRepository.findById(uuid)).thenReturn(Optional.of(post));

        PostEntity foundPost = postService.findById(uuid);

        assertNotNull(foundPost);
        assertEquals(uuid, foundPost.getUuid());
    }

    @Test
    void testFindById_PostNotFound() {
        when(postRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findById(uuid));
    }

    @Test
    void testFindAll_Success() {
        List<PostEntity> posts = Arrays.asList(post, new PostEntity());
        when(postRepository.findAll()).thenReturn(posts);

        List<PostEntity> foundPosts = postService.findAll();

        assertNotNull(foundPosts);
        assertEquals(2, foundPosts.size());
    }

    @Test
    void testDarLikePost_Success() {
        when(postRepository.findById(any(UUID.class))).thenReturn(Optional.of(post));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(likeRepository.save(any(LikeEntity.class))).thenReturn(new LikeEntity());

        String result = postService.darLikePost(uuid, uuid);

        assertEquals("Like no Post dado", result);
        verify(likeRepository, times(1)).save(any(LikeEntity.class));
    }

    @Test
    void testDarLikeComentario_Success() {
        CommentEntity comment = new CommentEntity();
        when(commentRepository.findById(any(UUID.class))).thenReturn(Optional.of(comment));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(likeRepository.save(any(LikeEntity.class))).thenReturn(new LikeEntity());

        String result = postService.darLikeComentario(uuid, uuid);

        assertEquals("Like no comentario dado", result);
        verify(likeRepository, times(1)).save(any(LikeEntity.class));
    }

    @Test
    void testDenunciarPost_Success() {
        when(postRepository.findById(any(UUID.class))).thenReturn(Optional.of(post));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(new ComplaintEntity());

        String result = postService.denunciarPost(uuid, uuid);

        assertEquals("Denuncia ao post feita", result);
        verify(complaintRepository, times(1)).save(any(ComplaintEntity.class));
    }

    @Test
    void testDenunciarComentario_Success() {
        CommentEntity comment = new CommentEntity();
        when(commentRepository.findById(any(UUID.class))).thenReturn(Optional.of(comment));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(new ComplaintEntity());

        String result = postService.denunciarComentario(uuid, uuid);

        assertEquals("Denuncia ao comentario feita", result);
        verify(complaintRepository, times(1)).save(any(ComplaintEntity.class));
    }
}
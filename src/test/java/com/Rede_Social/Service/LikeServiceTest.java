package com.Rede_Social.Service;

import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LikeServiceTest {

    @Autowired
    LikeService likeService;

    @MockBean
    LikeRepository likeRepository;

    LikeEntity like;
    UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        like = new LikeEntity();
        like.setUuid(uuid);
    }

    @Test
    void testSave_Success() {
        when(likeRepository.save(any(LikeEntity.class))).thenReturn(like);

        LikeEntity savedLike = likeService.save(like);

        assertNotNull(savedLike);
        assertEquals(uuid, savedLike.getUuid());
        verify(likeRepository, times(1)).save(like);
    }

    @Test
    void testSave_Exception() {
        when(likeRepository.save(any(LikeEntity.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> likeService.save(like));

        assertTrue(exception.getMessage().contains("Erro no service, não deu para salvar o like no repository"));
    }

    @Test
    void testUpdate_Success() {
        when(likeRepository.findById(uuid)).thenReturn(Optional.of(like));
        when(likeRepository.save(any(LikeEntity.class))).thenReturn(like);

        LikeEntity updatedLike = likeService.update(like, uuid);

        assertNotNull(updatedLike);
        assertEquals(uuid, updatedLike.getUuid());
        verify(likeRepository, times(1)).findById(uuid);
        verify(likeRepository, times(1)).save(like);
    }

    @Test
    void testUpdate_NotFound() {
        when(likeRepository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> likeService.update(like, uuid));

        assertTrue(exception.getMessage().contains("Like não existe no banco"));
    }

    @Test
    void testDelete_Success() {
        doNothing().when(likeRepository).deleteById(uuid);

        String result = likeService.delete(uuid);

        assertEquals("Like deletado", result);
        verify(likeRepository, times(1)).deleteById(uuid);
    }

    @Test
    void testDelete_Exception() {
        doThrow(new RuntimeException("Database error")).when(likeRepository).deleteById(uuid);

        Exception exception = assertThrows(RuntimeException.class, () -> likeService.delete(uuid));

        assertTrue(exception.getMessage().contains("Erro no service, não deu para deletar o like no repository"));
    }

    @Test
    void testFindById_Success() {
        when(likeRepository.findById(uuid)).thenReturn(Optional.of(like));

        LikeEntity foundLike = likeService.findById(uuid);

        assertNotNull(foundLike);
        assertEquals(uuid, foundLike.getUuid());
        verify(likeRepository, times(1)).findById(uuid);
    }

    @Test
    void testFindById_NotFound() {
        when(likeRepository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> likeService.findById(uuid));

        assertTrue(exception.getMessage().contains("Like não encontrado no banco"));
    }

    @Test
    void testFindAll_Success() {
        List<LikeEntity> likes = Arrays.asList(like, new LikeEntity());
        when(likeRepository.findAll()).thenReturn(likes);

        List<LikeEntity> foundLikes = likeService.findAll();

        assertNotNull(foundLikes);
        assertEquals(2, foundLikes.size());
        verify(likeRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_Exception() {
        when(likeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> likeService.findAll());

        assertTrue(exception.getMessage().contains("Erro no service, não deu para listar os likes no repository"));
    }
}
package com.Rede_Social.Service;

import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagService tagService;

    TagEntity tag;
    UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        tag = new TagEntity();
        tag.setUuid(uuid);
        tag.setNome("Test Tag");
    }

    @Test
    void testSave_Success() {
        when(tagRepository.save(any(TagEntity.class))).thenReturn(tag);

        TagEntity savedTag = tagService.save(tag);

        assertNotNull(savedTag);
        assertEquals(uuid, savedTag.getUuid());
        assertEquals("Test Tag", savedTag.getNome());
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    void testSave_Exception() {
        when(tagRepository.save(any(TagEntity.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> tagService.save(tag));

        assertTrue(exception.getMessage().contains("Erro no service, não deu para salvar a tag no repository"));
    }

    @Test
    void testUpdate_Success() {
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(TagEntity.class))).thenReturn(tag);

        TagEntity updatedTag = tagService.update(tag, uuid);

        assertNotNull(updatedTag);
        assertEquals(uuid, updatedTag.getUuid());
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    void testUpdate_TagNotFound() {
        when(tagRepository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> tagService.update(tag, uuid));

        assertTrue(exception.getMessage().contains("Tag não existe no banco"));
    }

    @Test
    void testDelete_Success() {
        doNothing().when(tagRepository).deleteById(uuid);

        String result = tagService.delete(uuid);

        assertEquals("Tag deletada", result);
        verify(tagRepository, times(1)).deleteById(uuid);
    }

    @Test
    void testDelete_Exception() {
        doThrow(new RuntimeException("Database error")).when(tagRepository).deleteById(uuid);

        Exception exception = assertThrows(RuntimeException.class, () -> tagService.delete(uuid));

        assertTrue(exception.getMessage().contains("Erro no service, não deu para deletar a tag no repository"));
    }

    @Test
    void testFindById_Success() {
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(tag));

        TagEntity foundTag = tagService.findById(uuid);

        assertNotNull(foundTag);
        assertEquals(uuid, foundTag.getUuid());
    }

    @Test
    void testFindById_TagNotFound() {
        when(tagRepository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> tagService.findById(uuid));

        assertEquals("Tag não encontrada no banco", exception.getMessage());
    }

    @Test
    void testFindAll_Success() {
        List<TagEntity> tags = Arrays.asList(tag, new TagEntity());
        when(tagRepository.findAll()).thenReturn(tags);

        List<TagEntity> foundTags = tagService.findAll();

        assertNotNull(foundTags);
        assertEquals(2, foundTags.size());
    }

    @Test
    void testFindAll_Exception() {
        when(tagRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> tagService.findAll());

        assertTrue(exception.getMessage().contains("Erro no service, não deu para listar a tags no repository"));
    }
}
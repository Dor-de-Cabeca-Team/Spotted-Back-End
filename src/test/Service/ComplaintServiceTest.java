package com.Rede_Social.Service;

import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Repository.ComplaintRepository;
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
public class ComplaintServiceTest {

    @Autowired
    ComplaintService complaintService;

    @MockBean
    ComplaintRepository complaintRepository;

    @Test
    public void testSave_Success() {
        ComplaintEntity complaint = new ComplaintEntity();
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(complaint);

        assertThrows(RuntimeException.class, () -> complaintService.save(complaint));

    }

    @Test
    public void testSave_Exception() {
        ComplaintEntity complaint = new ComplaintEntity();
        when(complaintRepository.save(any(ComplaintEntity.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> complaintService.save(complaint));
    }

    @Test
    public void testUpdate_Success() {
        UUID uuid = UUID.randomUUID();
        ComplaintEntity complaint = new ComplaintEntity();
        when(complaintRepository.findById(uuid)).thenReturn(Optional.of(new ComplaintEntity()));
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(complaint);

        ComplaintEntity result = complaintService.update(complaint, uuid);

        assertNotNull(result);
        assertEquals(uuid, complaint.getUuid());
        verify(complaintRepository, times(1)).save(complaint);
    }

    @Test
    public void testUpdate_NotFound() {
        UUID uuid = UUID.randomUUID();
        ComplaintEntity complaint = new ComplaintEntity();
        when(complaintRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> complaintService.update(complaint, uuid));
    }

    @Test
    public void testDelete_Success() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(complaintRepository).deleteById(uuid);

        String result = complaintService.delete(uuid);

        assertEquals("Reclamação deletada", result);
        verify(complaintRepository, times(1)).deleteById(uuid);
    }

    @Test
    public void testDelete_Exception() {
        UUID uuid = UUID.randomUUID();
        doThrow(new RuntimeException("Database error")).when(complaintRepository).deleteById(uuid);

        assertThrows(RuntimeException.class, () -> complaintService.delete(uuid));
    }

    @Test
    public void testFindById_Success() {
        UUID uuid = UUID.randomUUID();
        ComplaintEntity complaint = new ComplaintEntity();
        when(complaintRepository.findById(uuid)).thenReturn(Optional.of(complaint));

        ComplaintEntity result = complaintService.findById(uuid);

        assertNotNull(result);
        verify(complaintRepository, times(1)).findById(uuid);
    }

    @Test
    public void testFindById_NotFound() {
        UUID uuid = UUID.randomUUID();
        when(complaintRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> complaintService.findById(uuid));
    }

    @Test
    public void testFindAll_Success() {
        List<ComplaintEntity> complaints = Arrays.asList(new ComplaintEntity(), new ComplaintEntity());
        when(complaintRepository.findAll()).thenReturn(complaints);

        List<ComplaintEntity> result = complaintService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(complaintRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_Exception() {
        when(complaintRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> complaintService.findAll());
    }
}
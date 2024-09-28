package com.Rede_Social.Controller;

import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Service.ComplaintService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class ComplaintControllerTest {
    @Autowired
    ComplaintController complaintController;

    @MockBean
    ComplaintRepository complaintRepository;

    @Test
    void saveSuccess() {
        ComplaintEntity complaint = new ComplaintEntity();
        when(complaintRepository.save(complaint)).thenReturn(complaint);

        ResponseEntity<ComplaintEntity> response = complaintController.save(complaint);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complaint, response.getBody());
    }

    @Test
    void saveFailure() {
        ComplaintEntity complaint = new ComplaintEntity();

        when(complaintRepository.save(complaint)).thenThrow(new RuntimeException());

        ResponseEntity<ComplaintEntity> response = complaintController.save(complaint);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSuccess() {
        UUID uuid = UUID.randomUUID();
        ComplaintEntity complaint = new ComplaintEntity();

        when(complaintRepository.findById(uuid)).thenReturn(Optional.of(complaint));
        when(complaintRepository.save(complaint)).thenReturn(complaint);

        ResponseEntity<ComplaintEntity> response = complaintController.update(complaint, uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complaint, response.getBody());
    }

    @Test
    void updateFailure(){
        UUID uuid = UUID.randomUUID();
        ComplaintEntity complaint = new ComplaintEntity();

        when(complaintRepository.findById(uuid)).thenReturn(Optional.of(complaint));
        when(complaintRepository.save(complaint)).thenThrow(new RuntimeException());

        ResponseEntity<ComplaintEntity> response = complaintController.update(complaint, uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSuccess() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(complaintRepository).deleteById(uuid);

        ResponseEntity<String> response = complaintController.delete(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reclamação deletada", response.getBody());
    }

    @Test
    void deleteFailure() {
        UUID uuid = UUID.randomUUID();
        Mockito.doThrow(new RuntimeException()).when(complaintRepository).deleteById(uuid);

        ResponseEntity<String> response = complaintController.delete(uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findByIdSuccess() {
        UUID uuid = UUID.randomUUID();
        ComplaintEntity complaint = new ComplaintEntity();

        when(complaintRepository.findById(uuid)).thenReturn(Optional.of(complaint));

        ResponseEntity<ComplaintEntity> response = complaintController.findById(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complaint, response.getBody());
    }

    @Test
    void findByIdFailure() {
        UUID uuid = UUID.randomUUID();

        when(complaintRepository.findById(uuid)).thenReturn(Optional.empty());

        ResponseEntity<ComplaintEntity> response = complaintController.findById(uuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllSuccess() {
        List<ComplaintEntity> complaints = new ArrayList<>();
        ComplaintEntity complaint = new ComplaintEntity();
        complaints.add(complaint);

        when(complaintRepository.findAll()).thenReturn(complaints);

        ResponseEntity<List<ComplaintEntity>> response = complaintController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(complaints, response.getBody());
    }

    @Test
    void findAllFailure() {
        when(complaintRepository.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<ComplaintEntity>> response = complaintController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

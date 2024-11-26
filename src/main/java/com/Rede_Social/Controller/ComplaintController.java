package com.Rede_Social.Controller;

import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/complaint")
@CrossOrigin(origins = "http://localhost:4200")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<ComplaintEntity> save(@RequestBody ComplaintEntity complaint) {
        try {
            return ResponseEntity.ok(complaintService.save(complaint));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<ComplaintEntity> update(@RequestBody ComplaintEntity complaint, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(complaintService.update(complaint, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(complaintService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById")
    public ResponseEntity<ComplaintEntity> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(complaintService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<ComplaintEntity>> findAll() {
        try {
            return ResponseEntity.ok(complaintService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
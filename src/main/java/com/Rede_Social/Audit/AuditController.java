package com.Rede_Social.Audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditController {
    @Autowired
    AuditService auditService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<AuditEntry>> findAll(){
        try{
            List<AuditEntry> logs = auditService.findAll();
            return ResponseEntity.ok(logs);
        } catch(Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }
}

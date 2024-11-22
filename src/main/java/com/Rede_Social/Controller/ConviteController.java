package com.Rede_Social.Controller;

import com.Rede_Social.Entity.ConviteEntity;
import com.Rede_Social.Service.ConviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/convite")
@CrossOrigin(origins = "*")
public class ConviteController {
    @Autowired
    private ConviteService conviteService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<String> save() {
        try {
            String response = conviteService.save();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao gerar o convite: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            String response = conviteService.delete(uuid);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar o convite: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam UUID uuid) {
        try {
            ConviteEntity convite = conviteService.findById(uuid);
            return ResponseEntity.ok(convite);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Convite não encontrado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("findAll")
    public ResponseEntity<List<ConviteEntity>> findAll() {
        try {
            List<ConviteEntity> convites = conviteService.findAll();
            return ResponseEntity.ok(convites);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}

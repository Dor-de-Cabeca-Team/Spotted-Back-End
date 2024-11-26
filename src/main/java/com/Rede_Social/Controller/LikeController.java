package com.Rede_Social.Controller;

import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/like")
@CrossOrigin(origins = "http://localhost:4200")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<LikeEntity> save(@RequestBody LikeEntity like) {
        try {
            return ResponseEntity.ok(likeService.save(like));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<LikeEntity> update(@RequestBody LikeEntity like, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(likeService.update(like, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(likeService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById")
    public ResponseEntity<LikeEntity> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(likeService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<LikeEntity>> findAll() {
        try {
            return ResponseEntity.ok(likeService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
package com.Rede_Social.Controller;

import com.Rede_Social.DTO.Consulta.LikeDTO;
import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/like")
@CrossOrigin(origins = "*")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody LikeDTO like) {
        try {
            return ResponseEntity.ok(likeService.save(like));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody LikeDTO like, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(likeService.update(like, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(likeService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<LikeDTO> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(likeService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<LikeDTO>> findAll() {
        try {
            return ResponseEntity.ok(likeService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

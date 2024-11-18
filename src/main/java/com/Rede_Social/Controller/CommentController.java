package com.Rede_Social.Controller;

import com.Rede_Social.DTO.Consulta.CommentDTO;
import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")//request
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody CommentDTO comment) {
        try {
            return ResponseEntity.ok(commentService.save(comment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody CommentDTO comment, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(commentService.update(comment, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(commentService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<CommentDTO> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(commentService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CommentDTO>> findAll() {
        try {
            return ResponseEntity.ok(commentService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @GetMapping("/findAllValidosByPost_Uuid")
    public ResponseEntity<List<CommentDTO>> findAllByPostId(@RequestParam UUID idPost, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(commentService.findAllValidosByPost_Uuid(idPost, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

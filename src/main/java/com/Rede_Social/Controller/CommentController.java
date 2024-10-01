package com.Rede_Social.Controller;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentEntity> save(@RequestBody CommentEntity comment) {
        try {
            return ResponseEntity.ok(commentService.save(comment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/update")
    public ResponseEntity<CommentEntity> update(@RequestBody CommentEntity comment, @RequestParam UUID uuid) {
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
    public ResponseEntity<CommentEntity> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(commentService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CommentEntity>> findAll() {
        try {
            return ResponseEntity.ok(commentService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

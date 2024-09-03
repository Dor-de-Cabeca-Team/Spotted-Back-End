package com.Rede_Social.Controllers;

import com.Rede_Social.Entities.CommentEntity;
import com.Rede_Social.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentEntity> save(@RequestBody CommentEntity commentEntity) {
        try {
            return ResponseEntity.ok(commentService.save(commentEntity));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommentEntity> update(@RequestBody CommentEntity commentEntity, @PathVariable Long id){
        try {
            return ResponseEntity.ok(commentService.update(commentEntity, id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(commentService.delete(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CommentEntity>> findAll(){
        try {
            return ResponseEntity.ok(commentService.findAll());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<CommentEntity> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(commentService.findById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

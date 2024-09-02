package com.Rede_Social.Controllers;

import com.Rede_Social.Entities.PostEntity;
import com.Rede_Social.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/save")
    public ResponseEntity<PostEntity> save(@RequestBody PostEntity postEntity) {
        try {
            return ResponseEntity.ok(postService.save(postEntity));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostEntity> update(@RequestBody PostEntity postEntity, @PathVariable Long id){
        try {
            return ResponseEntity.ok(postService.update(postEntity, id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(postService.delete(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PostEntity>> findAll(){
        try {
            return ResponseEntity.ok(postService.findAll());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<PostEntity> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(postService.findById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

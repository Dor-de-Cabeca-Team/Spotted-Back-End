package com.Rede_Social.Controllers;

import com.Rede_Social.Entities.LikeEntity;
import com.Rede_Social.Services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/save")
    public ResponseEntity<LikeEntity> save(@RequestBody LikeEntity likeEntity) {
        try {
            return ResponseEntity.ok(likeService.save(likeEntity));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LikeEntity> update(@RequestBody LikeEntity likeEntity, @PathVariable Long id){
        try {
            return ResponseEntity.ok(likeService.update(likeEntity, id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(likeService.delete(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<LikeEntity>> findAll(){
        try {
            return ResponseEntity.ok(likeService.findAll());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<LikeEntity> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(likeService.findById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

package com.Rede_Social.Controllers;

import com.Rede_Social.Entities.TagEntity;
import com.Rede_Social.Services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/save")
    public ResponseEntity<TagEntity> save(@RequestBody TagEntity tagEntity) {
        try {
            return ResponseEntity.ok(tagService.save(tagEntity));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TagEntity> update(@RequestBody TagEntity tagEntity, @PathVariable Long id){
        try {
            return ResponseEntity.ok(tagService.update(tagEntity, id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(tagService.delete(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TagEntity>> findAll(){
        try {
            return ResponseEntity.ok(tagService.findAll());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<TagEntity> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(tagService.findById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

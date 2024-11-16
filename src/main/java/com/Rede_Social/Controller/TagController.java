package com.Rede_Social.Controller;

import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tag")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/save")
    public ResponseEntity<TagEntity> save(@RequestBody TagEntity tag) {
        try {
            return ResponseEntity.ok(tagService.save(tag));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<TagEntity> update(@RequestBody TagEntity tag, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(tagService.update(tag, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(tagService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<TagEntity> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(tagService.findById(uuid));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TagEntity>> findAll() {
        try {
            return ResponseEntity.ok(tagService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

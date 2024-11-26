package com.Rede_Social.Controller;

import com.Rede_Social.DTO.Consulta.TagDTO;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tag")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody TagDTO tag) {
        try {
            return ResponseEntity.ok(tagService.save(tag));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById")
    public ResponseEntity<TagDTO> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(tagService.findById(uuid));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<TagDTO>> findAll() {
        try {
            return ResponseEntity.ok(tagService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestBody TagDTO tag, @RequestParam UUID uuid) {
//        try {
//            return ResponseEntity.ok(tagService.update(tag, uuid));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
//        try {
//            return ResponseEntity.ok(tagService.delete(uuid));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

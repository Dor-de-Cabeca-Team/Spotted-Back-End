package com.Rede_Social.Controllers;

import com.Rede_Social.Entities.UserEntity;
import com.Rede_Social.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity userEntity) {
        try {
            return ResponseEntity.ok(userService.save(userEntity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserEntity> update(@RequestBody UserEntity userEntity, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.update(userEntity, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar o ticket: " + e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserEntity>> findAll() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<UserEntity> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

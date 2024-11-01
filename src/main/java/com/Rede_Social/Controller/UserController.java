package com.Rede_Social.Controller;

import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity user) {
        try {
            return ResponseEntity.ok(userService.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserEntity> update(@RequestBody UserEntity user, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(userService.update(user, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(userService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<UserEntity> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(userService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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

    @GetMapping("/validar-conta")
    public ResponseEntity<String> validarConta(@RequestParam UUID idUser,@RequestParam String hash) {
        boolean isValid = userService.validarConta(idUser,hash);
        if (isValid) {
            return ResponseEntity.ok("Conta validada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha na validação da conta.");
        }
    }

    @PostMapping("/login-provisorio")
    public ResponseEntity<UserEntity> loginProvisorio(@RequestParam String email, @RequestParam String senha) {
        try {
            return ResponseEntity.ok(userService.loginProvisorio(email,senha));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

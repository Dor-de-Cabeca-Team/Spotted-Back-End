package com.Rede_Social.Controller;

import com.Rede_Social.DTO.Consulta.UserDTO;
import com.Rede_Social.DTO.Criacao.UserCriacaoDTO;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserCriacaoDTO user, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(userService.update(user, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(userService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @GetMapping("/findById")
    public ResponseEntity<UserDTO> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(userService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDTO>> findAll() {
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


}

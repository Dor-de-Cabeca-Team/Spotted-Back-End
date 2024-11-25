package com.Rede_Social.Auth;

import com.Rede_Social.DTO.Request.TrocarSenhaRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> logar(@RequestBody Login login) {
        try {
            String token = authService.logar(login);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação.");
        }
    }

    @PostMapping("/register")
	public ResponseEntity registrar(@RequestBody Register dado) {
		try {
			authService.registrar(dado);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("erro ao processar o registro.");
		}
	}

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @PostMapping("/trocar-senha")
    public ResponseEntity<String> trocarSenha(@RequestBody TrocarSenhaRequestDTO dados) {
        try {
            authService.trocarSenha(dados);
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar a troca de senha: " + e.getMessage());
        }
    }
}

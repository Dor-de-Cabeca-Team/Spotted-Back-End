//AuthenticationService.java
package com.Rede_Social.Auth;

import com.Rede_Social.Auth.PasswordReset.PasswordResetToken;
import com.Rede_Social.Auth.PasswordReset.PasswordResetTokenRepository;
import com.Rede_Social.Config.JwtServiceGenerator;
import com.Rede_Social.DTO.Request.TrocarSenhaRequestDTO;
import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.Role;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class AuthService {
	
	@Autowired
	private AuthRepository repository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

    public String logar(Login login){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getPassword()
                )
        );
        UserEntity user = repository.findByEmail(login.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
		if(user.getAtivo()) {
			return jwtService.generateToken(user);
		} else{
			EmailEntity email = emailService.criarEmail(user);
			emailService.enviaEmail(email);

			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email não verificado");
		}
	}

    public void registrar(Register dado) throws Exception {
		try {
			if (repository.findByEmail(dado.email()).isPresent()) {
				throw new IllegalArgumentException("Email ja esta sendo usado");
			}

			String encryptedSenha = passwordEncoder.encode(dado.senha());
			UserEntity novoUsuario = new UserEntity(Role.USUARIO, dado.nome(), dado.idade(), dado.email(), encryptedSenha, false);

			userRepository.save(novoUsuario);

			EmailEntity email = emailService.criarEmail(novoUsuario);
			emailService.enviaEmail(email);

		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception("erro ao registrar user", e);
		}
	}

	public String trocarSenha(TrocarSenhaRequestDTO dados) {
		try {
			UserEntity existingUser = userRepository.findById(dados.idUser()).orElseThrow(UserNotFoundException::new);

			String senhaDoUsuario = existingUser.getSenha();

			if (!passwordEncoder.matches(dados.senhaAntiga(), senhaDoUsuario)) {
				throw new IllegalArgumentException("A senha antiga está incorreta");
			}

			existingUser.setSenha(passwordEncoder.encode(dados.senhaNova()));
			userRepository.save(existingUser);

			return "Senha atualizada com sucesso";
		} catch (UserNotFoundException | IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Erro no service, não foi possível atualizar o usuário: " + e.getMessage());
			throw new RuntimeException("Erro no service, não foi possível atualizar o usuário: " + e.getMessage());
		}
	}

	public void solicitarRedefinicaoSenha(String email) {
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Usuário com o e-mail fornecido não encontrado."));


		String token = UUID.randomUUID().toString();
		PasswordResetToken resetToken = new PasswordResetToken(
				token,
				user,
				LocalDateTime.now().plusMinutes(30) // Token válido por 30 minutos
		);

		passwordResetTokenRepository.save(resetToken);

		EmailEntity emailEntity = emailService.criarEmailRedefinicaoSenha(user, token);
		emailService.enviaEmail(emailEntity);
	}

	public void redefinirSenha(String token, String novaSenha) {
		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado."));

		if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Token expirado.");
		}

		UserEntity user = resetToken.getUser();
		user.setSenha(passwordEncoder.encode(novaSenha));
		userRepository.save(user);

		passwordResetTokenRepository.delete(resetToken);
	}
}
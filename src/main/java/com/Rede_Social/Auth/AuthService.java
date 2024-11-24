//AuthenticationService.java
package com.Rede_Social.Auth;

import com.Rede_Social.Config.JwtServiceGenerator;
import com.Rede_Social.DTO.Request.TrocarSenhaRequestDTO;
import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.Role;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
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

    public String logar(Login login){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getPassword()
                )
        );
        UserEntity user = repository.findByEmail(login.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        return jwtService.generateToken(user);
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
}

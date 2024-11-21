//AuthenticationService.java
package com.Rede_Social.Auth;

import com.Rede_Social.Config.JwtServiceGenerator;
import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.Role;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;


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
}

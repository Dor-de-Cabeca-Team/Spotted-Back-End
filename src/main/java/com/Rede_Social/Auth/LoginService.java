//AuthenticationService.java
package com.Rede_Social.Auth;

import com.Rede_Social.Config.JwtServiceGenerator;
import com.Rede_Social.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class LoginService {
	
	@Autowired
	private LoginRepository repository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;


	public String logar(Login login) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getEmail(),
						login.getPassword()
						)
				);
		UserEntity user = repository.findByEmail(login.getEmail()).get();
		String jwtToken = jwtService.generateToken(user);
		
		return jwtToken;
	}

}

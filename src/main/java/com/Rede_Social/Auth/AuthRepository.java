package com.Rede_Social.Auth;

import java.util.Optional;
import java.util.UUID;

import com.Rede_Social.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthRepository extends JpaRepository<UserEntity, UUID>{

	public Optional<UserEntity> findByEmail(String login);
	
}

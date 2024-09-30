package com.Rede_Social.Repository;

import com.Rede_Social.Entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailEntity, UUID> {
}

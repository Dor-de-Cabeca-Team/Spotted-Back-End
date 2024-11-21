package com.Rede_Social.Repository;

import com.Rede_Social.Entity.ConviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConviteRepository extends JpaRepository<ConviteEntity, UUID> {
}

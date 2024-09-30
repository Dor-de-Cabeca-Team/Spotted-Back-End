package com.Rede_Social.Repository;

import com.Rede_Social.Entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, UUID> {
}

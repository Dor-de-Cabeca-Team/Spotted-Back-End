package com.Rede_Social.Audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditRepository extends JpaRepository<AuditEntry, UUID> {

}

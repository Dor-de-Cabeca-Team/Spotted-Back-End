package com.Rede_Social.Audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class AuditService {
    @Autowired
    AuditRepository auditRepository;

    public void logAcao(String acao, String email){
        AuditEntry log = new AuditEntry();
        log.setAcao(acao);
        log.setEmail(email);
        log.setData(Timestamp.from(Instant.now()));
        auditRepository.save(log);
    }

    public List<AuditEntry> findAll(){
        return auditRepository.findAll();
    }
}

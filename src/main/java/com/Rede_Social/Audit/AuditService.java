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

    public void logAcao(String email, String acao, String conteudo){
        AuditEntry log = new AuditEntry();
        log.setEmail(email);
        log.setAcao(acao);
        log.setConteudo(conteudo);
        log.setData(Timestamp.from(Instant.now()));
        auditRepository.save(log);
    }

    public List<AuditEntry> findAll(){
        return auditRepository.findAll();
    }
}

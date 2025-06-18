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
        try {
            AuditEntry log = new AuditEntry();
            log.setEmail(email);
            log.setAcao(acao);
            log.setConteudo(conteudo);
            log.setData(Timestamp.from(Instant.now()));
            auditRepository.save(log);
        } catch (Exception e){
            throw new RuntimeException("Erro ao salvar o log da ação do usuario");
        }
    }

    public List<AuditEntry> findAll(){
        try{
            return auditRepository.findAll();
        }catch (Exception e){
            System.out.println("Erro no service, não deu para listar os logs: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os logs: " + e.getMessage());
        }
    }
}

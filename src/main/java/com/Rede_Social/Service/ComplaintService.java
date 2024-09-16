package com.Rede_Social.Service;

import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    public ComplaintEntity save(ComplaintEntity complaint) {
        try {
            return complaintRepository.save(complaint);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a reclamação no repository: " + e.getMessage());
            return new ComplaintEntity();
        }
    }

    public ComplaintEntity update(ComplaintEntity complaint, UUID uuid) {
        try {
            complaintRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Reclamação não existe no banco"));
            complaint.setUuid(uuid);
            return complaintRepository.save(complaint);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar a reclamação no repository: " + e.getMessage());
            return new ComplaintEntity();
        }
    }

    public String delete(UUID uuid) {
        try {
            complaintRepository.deleteById(uuid);
            return "Reclamação deletada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar a reclamação no repository: " + e.getMessage());
            return "Não deu para deletar a reclamação, erro no service";
        }
    }

    public ComplaintEntity findById(UUID uuid) {
        try {
            return complaintRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Reclamação não encontrada no banco"));
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para encontrar a reclamação no repository: " + e.getMessage());
            return new ComplaintEntity();
        }
    }

    public List<ComplaintEntity> findAll() {
        try {
            return complaintRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar as reclamações do banco: " + e.getMessage());
            return List.of();
        }
    }
}

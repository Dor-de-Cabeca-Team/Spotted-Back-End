package com.Rede_Social.Service;

import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public LikeEntity save(LikeEntity like) {
        try {
            return likeRepository.save(like);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o like no repository: " + e.getMessage());
        }
    }

    public LikeEntity update(LikeEntity like, UUID uuid) {
        try {
            likeRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Like não existe no banco"));
            like.setUuid(uuid);
            return likeRepository.save(like);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar o like no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            likeRepository.deleteById(uuid);
            return "Like deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar o like no repository: " + e.getMessage());
        }
    }

    public LikeEntity findById(UUID uuid) {
        return likeRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Like não encontrado no banco"));
    }

    public List<LikeEntity> findAll() {
        try {
            return likeRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os likes do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os likes no repository: " + e.getMessage());
        }
    }
}

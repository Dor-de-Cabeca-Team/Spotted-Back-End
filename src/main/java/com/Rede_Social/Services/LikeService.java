package com.Rede_Social.Services;

import com.Rede_Social.Entities.LikeEntity;
import com.Rede_Social.Repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    //save
    public LikeEntity save(LikeEntity likeEntity){
        try{
            return likeRepository.save(likeEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new LikeEntity();
        }
    }

    //update
    public LikeEntity update(LikeEntity likeEntity, Long id){
        try{
            likeEntity.setId(id);
            return likeRepository.save(likeEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new LikeEntity();
        }
    }

    //delete
    public String delete(Long id){
        try {
            if (likeRepository.findById(id).isPresent()){
                likeRepository.deleteById(id);
                return "Like deletado com sucesso!";
            } else {
                return "Like não encontrado";
            }
        }catch (Exception e){
            System.out.println("Erro ao deletar Like: " + e.getMessage());
            return "Erro ao deletar like.";
        }
    }

    //findall
    public List<LikeEntity> findAll(){
        try {
            return likeRepository.findAll();
        } catch (Exception e){
            System.out.println("Erro ao retornar lista de likes: " + e.getMessage());
            return List.of();
        }
    }

    //findbyid
    public LikeEntity findById(Long id){
        try {
            return likeRepository.findById(id).
                    orElseThrow(() -> {
                        System.out.println("Like não encontrado com o ID: " + id);
                        return new RuntimeException("Like não encontrado");
                    });
        } catch (Exception e) {
            System.out.println("Erro ao buscar like: " + e.getMessage());
            return new LikeEntity();
        }
    }
}

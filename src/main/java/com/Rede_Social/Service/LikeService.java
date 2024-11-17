package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.LikeDTO;
import com.Rede_Social.DTO.Mapper.LikeDTOMapper;
import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public LikeDTO save(LikeEntity like) {
        try {
            LikeEntity likeEntity = likeRepository.save(like);
            return LikeDTOMapper.toLikeDto(likeEntity);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o like no repository: " + e.getMessage());
        }
    }

    public LikeDTO update(LikeEntity like, UUID uuid) {
        try {
            likeRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Like não existe no banco"));
            like.setUuid(uuid);
            LikeEntity likeEntity = likeRepository.save(like);
            return LikeDTOMapper.toLikeDto(likeEntity);
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

    public LikeDTO findById(UUID uuid) {
        LikeEntity likeEntity = likeRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Like não encontrado no banco"));
        return LikeDTOMapper.toLikeDto(likeEntity);
    }

    public List<LikeDTO> findAll() {
        try {
            List<LikeEntity> likes = likeRepository.findAll();
            List<LikeDTO> likesDTO = new ArrayList<>();
            for(LikeEntity likeEntity : likes) {
                likesDTO.add(LikeDTOMapper.toLikeDto(likeEntity));
            }
            return likesDTO;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os likes do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os likes no repository: " + e.getMessage());
        }
    }
}

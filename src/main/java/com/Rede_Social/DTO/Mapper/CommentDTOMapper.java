package com.Rede_Social.DTO.Mapper;

import com.Rede_Social.DTO.Consulta.CommentDTO;
import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Repository.LikeRepository;

import java.util.UUID;

public class CommentDTOMapper {
    public static CommentDTO toCommentDto(CommentEntity commentEntity, UUID idUser, LikeRepository likeRepository) {
        boolean isLiked = likeRepository.findByPostAndUser(commentEntity.getUuid(), idUser).isPresent();
        return new CommentDTO(
                commentEntity.getUuid(),
                commentEntity.getData(),
                isLiked,
                commentEntity.getConteudo(),
                commentEntity.getLikes() != null ? commentEntity.getLikes().size() : 0,
                commentEntity.getProfileAnimal()
        );
    }
}

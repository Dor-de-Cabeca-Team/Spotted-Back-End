package com.Rede_Social.DTO.Mapper;

import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.LikeRepository;

import java.util.UUID;

public class PostDTOMapper {

    public static PostDTO toPostDto(PostEntity postEntity, UUID idUser, LikeRepository likeRepository, ComplaintRepository complaintRepository) {
        boolean isLiked = likeRepository.findByPostAndUser(postEntity.getUuid(), idUser).isPresent();
        boolean isReported = complaintRepository.findByCommentAndUser(postEntity.getUuid(), idUser).isPresent();

        return new PostDTO(
                postEntity.getUuid(),
                postEntity.getConteudo(),
                isLiked,
                isReported,
                postEntity.getLikes() != null ? postEntity.getLikes().size() : 0,
                postEntity.getData(),
                postEntity.getProfileAnimal()
        );
    }
}

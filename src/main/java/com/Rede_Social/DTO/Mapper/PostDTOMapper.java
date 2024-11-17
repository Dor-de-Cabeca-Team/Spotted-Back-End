package com.Rede_Social.DTO.Mapper;

import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostDTOMapper {

    public static PostDTO toPostDto(PostEntity postEntity, UUID idUser, LikeRepository likeRepository, ComplaintRepository complaintRepository) {
        boolean isLiked = likeRepository.findByPostAndUser(postEntity.getUuid(), idUser).isPresent();
        boolean isReported = complaintRepository.findByCommentAndUser(postEntity.getUuid(), idUser).isPresent();

        List<UUID> tagIds = postEntity.getTags() != null ?
                postEntity.getTags().stream().map(TagEntity::getUuid).toList() : new ArrayList<>();

        List<UUID> commentIds = postEntity.getComments() != null ?
                postEntity.getComments().stream().map(CommentEntity::getUuid).toList() : new ArrayList<>();

        return new PostDTO(
                postEntity.getUuid(),
                postEntity.getConteudo(),
                isLiked,
                isReported,
                postEntity.getLikes() != null ? postEntity.getLikes().size() : 0,
                postEntity.getData(),
                postEntity.getProfileAnimal(),
                postEntity.getUser() != null ? postEntity.getUser().getUuid() : null,
                tagIds,
                commentIds
        );
    }
}


package com.Rede_Social.DTO.Mapper.Top10PostsComLike;

import com.Rede_Social.DTO.Consulta.Top10PostsComLike.PostConsultaTop10DTO;
import com.Rede_Social.DTO.Mapper.CommentDTOMapper;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.LikeRepository;

import java.util.UUID;

public class PostTop10Mapper {
    public static PostConsultaTop10DTO toPostConsultaDTO(PostEntity postEntity, UUID idUser, LikeRepository likeRepository, ComplaintRepository complaintRepository) {
        return new PostConsultaTop10DTO(
                postEntity.getUuid(),
                postEntity.getConteudo(),
                TagTop10Mapper.toTagConsultDTOList(postEntity.getTags()),
                CommentDTOMapper.toCommentDtoList(postEntity.getComments(), idUser, likeRepository, complaintRepository)
        );
    }
}

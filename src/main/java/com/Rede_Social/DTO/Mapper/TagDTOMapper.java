package com.Rede_Social.DTO.Mapper;


import com.Rede_Social.DTO.Consulta.CommentDTO;
import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.DTO.Consulta.TagDTO;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TagDTOMapper {
    public static TagDTO toTagDto(TagEntity tagEntity, UUID idUser, LikeRepository likeRepository, ComplaintRepository complaintRepository) {
        List<PostDTO> postDTOS = tagEntity.getPosts() != null ?
                tagEntity.getPosts().stream()
                        .map(postEntity -> PostDTOMapper.toPostDto(postEntity, idUser, likeRepository, complaintRepository))
                        .collect(Collectors.toList()) : List.of();
        return new TagDTO(
                tagEntity.getNome(),
                postDTOS
        );
    }
}

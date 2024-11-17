package com.Rede_Social.DTO.Mapper;


import com.Rede_Social.DTO.Consulta.TagDTO;
import com.Rede_Social.Entity.TagEntity;

import java.util.stream.Collectors;

public class TagDTOMapper {
    public static TagDTO toTagDto(TagEntity tagEntity) {
        return new TagDTO(
                tagEntity.getNome(),
                tagEntity.getPosts().stream()
                        .map(post -> post.getUuid())
                        .collect(Collectors.toList())
        );
    }
}

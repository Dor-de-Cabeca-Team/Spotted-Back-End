package com.Rede_Social.DTO.Mapper.Top10PostsComLike;

import com.Rede_Social.DTO.Consulta.Top10PostsComLike.TagConsultaTop10DTO;
import com.Rede_Social.Entity.TagEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TagTop10Mapper {
    public static TagConsultaTop10DTO toTagConsultDTO(TagEntity tagEntity) {
        return new TagConsultaTop10DTO(
                tagEntity.getUuid(),
                tagEntity.getNome()
        );
    }

    public static List<TagConsultaTop10DTO> toTagConsultDTOList(List<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(TagTop10Mapper::toTagConsultDTO)
                .collect(Collectors.toList());
    }
}

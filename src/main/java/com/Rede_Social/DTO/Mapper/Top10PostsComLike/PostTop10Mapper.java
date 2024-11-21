package com.Rede_Social.DTO.Mapper.Top10PostsComLike;

import com.Rede_Social.DTO.Consulta.Top10PostsComLike.PostConsultaTop10DTO;
import com.Rede_Social.Entity.PostEntity;

public class PostTop10Mapper {
    public static PostConsultaTop10DTO toPostConsultaDTO(PostEntity postEntity) {
        return new PostConsultaTop10DTO(
                postEntity.getUuid(),
                postEntity.getConteudo(),
                TagTop10Mapper.toTagConsultDTOList(postEntity.getTags())  // Chamando o mapper de tags
        );
    }
}
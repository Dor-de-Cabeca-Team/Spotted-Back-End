package com.Rede_Social.DTO.Mapper;
import com.Rede_Social.DTO.Consulta.LikeDTO;
import com.Rede_Social.Entity.LikeEntity;

import java.util.UUID;

public class LikeDTOMapper {
    public static LikeDTO toLikeDto(LikeEntity likeEntity) {
        return new LikeDTO(
                likeEntity.getUser().getUuid(),
                likeEntity.getPost().getUuid(),
                likeEntity.getComment().getUuid()
                );
    }
}

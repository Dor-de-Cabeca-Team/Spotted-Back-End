package com.Rede_Social.DTO.Mapper;

import com.Rede_Social.DTO.Consulta.ComplaintDTO;
import com.Rede_Social.DTO.Consulta.LikeDTO;
import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Entity.LikeEntity;

public class ComplaintDTOMapper {
    public static ComplaintDTO toComplaintDto(ComplaintEntity complaintEntity) {
        return new ComplaintDTO(
                complaintEntity.getUser().getUuid(),
                complaintEntity.getPost().getUuid(),
                complaintEntity.getComment().getUuid()
        );
    }
}

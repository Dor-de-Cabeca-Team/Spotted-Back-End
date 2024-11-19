package com.Rede_Social.DTO.Mapper;

import com.Rede_Social.DTO.Consulta.ComplaintDTO;
import com.Rede_Social.Entity.ComplaintEntity;

public class ComplaintDTOMapper {
    public static ComplaintDTO toComplaintDto(ComplaintEntity complaintEntity) {
        return new ComplaintDTO(
                complaintEntity.getUser().getUuid(),
                complaintEntity.getPost().getUuid(),
                complaintEntity.getComment().getUuid()
        );
    }
}

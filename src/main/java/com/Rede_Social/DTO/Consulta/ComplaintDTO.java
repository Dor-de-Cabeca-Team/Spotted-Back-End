package com.Rede_Social.DTO.Consulta;

import java.util.UUID;

public record ComplaintDTO(UUID idUser, UUID idPost, UUID idComment) {
}

package com.Rede_Social.DTO.Consulta;

import java.util.UUID;

public record LikeDTO(UUID idUser, UUID idPost, UUID idComment) {
}

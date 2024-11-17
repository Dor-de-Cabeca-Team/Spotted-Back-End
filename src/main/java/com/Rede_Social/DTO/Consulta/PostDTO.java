package com.Rede_Social.DTO.Consulta;

import java.time.Instant;
import java.util.UUID;

public record PostDTO(UUID idPost, String conteudo, boolean isLiked, Integer likeCount, Instant data, Integer profileAnimal) {
}

package com.Rede_Social.DTO.Consulta;

import java.time.Instant;
import java.util.UUID;

public record CommentDTO(UUID idComment, Instant data, boolean isLiked, String conteudo, Integer likeCount, Integer profileAnimal) {
}

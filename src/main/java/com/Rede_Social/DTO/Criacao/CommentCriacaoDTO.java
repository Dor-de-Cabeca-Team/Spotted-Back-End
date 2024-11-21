package com.Rede_Social.DTO.Criacao;

import java.util.UUID;

public record CommentCriacaoDTO(UUID id, String conteudo, UUID post, UUID user) {

}

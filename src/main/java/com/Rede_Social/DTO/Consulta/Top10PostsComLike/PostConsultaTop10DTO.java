package com.Rede_Social.DTO.Consulta.Top10PostsComLike;

import com.Rede_Social.DTO.Consulta.CommentDTO;

import java.util.List;
import java.util.UUID;

public record PostConsultaTop10DTO(UUID id, String conteudo, List<TagConsultaTop10DTO> tags, List<CommentDTO> comments) {
}

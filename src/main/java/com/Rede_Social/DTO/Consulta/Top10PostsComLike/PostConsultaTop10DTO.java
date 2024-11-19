package com.Rede_Social.DTO.Consulta.Top10PostsComLike;

import java.util.List;
import java.util.UUID;

public record PostConsultaTop10DTO(UUID id, String conteudo, List<TagConsultaTop10DTO> tags) {
}

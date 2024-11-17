package com.Rede_Social.DTO.Consulta;

import com.Rede_Social.Entity.PostEntity;

import java.util.List;
import java.util.UUID;

public record TagDTO(String nome, List<UUID> idPost) {
}

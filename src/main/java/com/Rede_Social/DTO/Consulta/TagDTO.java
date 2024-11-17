package com.Rede_Social.DTO.Consulta;

import com.Rede_Social.Entity.PostEntity;

import java.util.List;

public record TagDTO(String nome, List<PostEntity> Post) {
}

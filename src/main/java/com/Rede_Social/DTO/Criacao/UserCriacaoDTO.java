package com.Rede_Social.DTO.Criacao;

import com.Rede_Social.Entity.Enum.Role;

public record UserCriacaoDTO(String nome, String email, String senha, Integer idade) {
}

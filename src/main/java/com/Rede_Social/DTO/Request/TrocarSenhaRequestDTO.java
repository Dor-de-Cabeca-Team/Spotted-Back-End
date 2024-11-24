package com.Rede_Social.DTO.Request;

import java.util.UUID;

public record TrocarSenhaRequestDTO(UUID idUser, String senhaAntiga, String senhaNova) {
}

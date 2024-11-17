package com.Rede_Social.DTO.Mapper;

import com.Rede_Social.DTO.Consulta.UserDTO;
import com.Rede_Social.Entity.UserEntity;

public class UserDTOMapper {
    public static UserDTO toUserDto(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getNome(),
                userEntity.getEmail(),
                userEntity.getSenha(),
                userEntity.getIdade(),
                userEntity.getAtivo(),
                userEntity.getRole()
        );
    }
}

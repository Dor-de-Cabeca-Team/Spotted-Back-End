package com.Rede_Social.DTO.Consulta;

import com.Rede_Social.Entity.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String nome;
    String email;
    boolean activated;
    Role role;
}

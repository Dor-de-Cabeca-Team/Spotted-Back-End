package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.UserDTO;
import com.Rede_Social.DTO.Criacao.UserCriacaoDTO;
import com.Rede_Social.DTO.Mapper.UserDTOMapper;
import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.Role;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public String update(UserCriacaoDTO user, UUID uuid) {
        try {
            UserEntity existingUser = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);
            existingUser.setNome(user.nome());
            existingUser.setIdade(user.idade());
            existingUser.setEmail(user.email());

            userRepository.save(existingUser);

            return "Usuário atualizado";
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar o usuário no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar o usuário no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            userRepository.deleteById(uuid);
            return "Usuário deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar o usuário no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar o usuário no repository: " + e.getMessage());
        }
    }

    public UserDTO findById(UUID uuid) {
        UserEntity user = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);
        return UserDTOMapper.toUserDto(user);
    }

    public List<UserDTO> findAll() {
        try {
            List<UserEntity> users = userRepository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            for (UserEntity user : users) {
                userDTOS.add(UserDTOMapper.toUserDto(user));
            }
            return userDTOS;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os usuários do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar todos os usuários: " + e.getMessage());
        }
    }

    public boolean validarConta(UUID idUser, String hashRecebido) {
        UserEntity usuario = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);
        String hashGerado = EmailService.generateHash(usuario.getNome(), usuario.getEmail());

        if (hashGerado.equals(hashRecebido)) {
            usuario.setAtivo(true);
            userRepository.save(usuario);
            return true;
        }
        return false;
    }
}
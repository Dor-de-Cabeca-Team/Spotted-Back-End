package com.Rede_Social.Services;

import com.Rede_Social.Entities.UserEntity;
import com.Rede_Social.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity save(UserEntity userEntity) {
        try {
            return userRepository.save(userEntity);
        } catch (Exception e) {
            System.out.println("Erro ao salvar o usuário: " + e.getMessage());
            return new UserEntity();
        }
    }

    public UserEntity update(UserEntity userEntity, Long id) {
        try {
            userEntity.setId(id);
            return userRepository.save(userEntity);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar o usuário: " + e.getMessage());
            return new UserEntity();
        }
    }

    public String delete(Long id) {
        try {
            if (userRepository.findById(id).isPresent()) {
                userRepository.deleteById(id);
                return "Usuário deletado com sucesso!";
            } else {
                return "Usuário não encontrado";
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar o usuário: " + e.getMessage());
            return "Erro ao deletar o usuário";
        }
    }

    public List<UserEntity> findAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro ao retornar a lista de usuários: " + e.getMessage());
            return List.of();
        }
    }

    public UserEntity findById(Long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> {
                        System.out.println("Usuário não encontrado com o ID: " + id);
                        return new RuntimeException("Usuário não encontrado");
                    });
        } catch (Exception e) {
            System.out.println("Erro ao buscar o usuário: " + e.getMessage());
            return new UserEntity();
        }
    }
}

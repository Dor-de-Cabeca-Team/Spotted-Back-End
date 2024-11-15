package com.Rede_Social.Service;

import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.Role;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public UserEntity save(UserEntity user) {
        try {
            userRepository.save(user);
            if (!user.getEmail().isEmpty()) {
                EmailEntity email = emailService.criarEmail(user);
                emailService.enviaEmail(email);
            }
            user.setRole(Role.ADMIN);
            return user;
        } catch (Exception e) {
            System.out.println("Erro no service, n deu para salvar o usuario no repository" + e.getMessage());
            throw new RuntimeException("Erro no service, n deu para salvar o usuario no repository" + e.getMessage());
        }
    }

    public UserEntity update(UserEntity user, UUID uuid) {
        try {
            userRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException());
            user.setUuid(uuid);
            return userRepository.save(user);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Erro no service, n deu para atualizar o usuario no repository" + e.getMessage());
            throw new RuntimeException("Erro no service, n deu para atualizar o usuario no repository " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            userRepository.deleteById(uuid);
            return "usuario deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, n deu para atualizar o usuario no repository" + e.getMessage());
            throw new RuntimeException("Erro no service, n deu para deletar o usuario no repository" + e.getMessage());
        }
    }

    public UserEntity findById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException());
    }

    public List<UserEntity> findAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, n deu para listar os usuarios do banco" + e.getMessage());
            throw new RuntimeException("Erro no service, n deu para listar todos users " + e.getMessage());
        }
    }

    public boolean validarConta(UUID idUser, String hashRecebido) {
        UserEntity usuario = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException());

        String hashGerado = EmailService.generateHash(usuario.getNome(), usuario.getEmail());

        if (hashGerado.equals(hashRecebido)) {
            usuario.setAtivo(true);
            userRepository.save(usuario);
            return true;
        }
        return false;
    }

    public UserEntity loginProvisorio(String email, String senha) {
        try {
            return userRepository.findByEmailAndSenha(email, senha)
                    .orElseThrow(UserNotFoundException::new);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro no service, não foi possível realizar o login: " + e.getMessage());
        }

    }
}

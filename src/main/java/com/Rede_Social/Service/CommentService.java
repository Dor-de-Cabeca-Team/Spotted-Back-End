package com.Rede_Social.Service;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.CommentRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.AI.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GeminiService geminiService;

    public CommentEntity save(CommentEntity comment) {
        try {
            UserEntity user = userRepository.findById(comment.getUser().getUuid()).orElseThrow(UserNotFoundException::new);
            PostEntity post = postRepository.findById(comment.getPost().getUuid()).orElseThrow(PostNotFoundException::new);

            System.out.println(comment);
            comment.setValido(geminiService.validadeAI(comment.getConteudo()));

            comment.setData(Instant.now());
comment.setUser(user);
comment.setPost(post);
            return commentRepository.save(comment);

        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o comentário no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o comentário no repository: " + e.getMessage());
        }
    }

    public CommentEntity update(CommentEntity comment, UUID uuid) {
        try {
            commentRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Comentário não existe no banco"));
            comment.setUuid(uuid);
            return commentRepository.save(comment);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar o comentário no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar o comentário no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            commentRepository.deleteById(uuid);
            return "Comentário deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar o comentário no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar o comentário no repository: " + e.getMessage());
        }
    }

    public CommentEntity findById(UUID uuid) {
        return commentRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Comentário não encontrado no banco"));
    }

    public List<CommentEntity> findAll() {
        try {
            return commentRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os comentários do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os comentarios: " + e.getMessage());
        }
    }

    public List<CommentEntity> findAllByPost_Uuid(UUID uuid) {
        try {
            return commentRepository.findAllByPost_Uuid(uuid);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os comentarios de um post específico" + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os comentarios de um post específico" + e.getMessage());
        }
    }
}

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

    public CommentEntity save(UUID idPost, UUID idUser, String comment) {
        try {
            UserEntity user = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);
            PostEntity post = postRepository.findById(idPost).orElseThrow(PostNotFoundException::new);

            boolean valido = geminiService.validadeAI(comment);

            CommentEntity comentario = new CommentEntity(UUID.randomUUID(), Instant.now(), comment, valido, null, null, user, post);

            if (comentario == null) {
                System.out.println("Comentário é nulo antes de salvar!");
            }

            return commentRepository.save(comentario);

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
}

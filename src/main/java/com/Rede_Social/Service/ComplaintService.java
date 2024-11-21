package com.Rede_Social.Service;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.CommentRepository;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ComplaintEntity save(ComplaintEntity complaint) {
        try {
            UserEntity user = userRepository.findById(complaint.getUser().getUuid()).orElseThrow(UserNotFoundException::new);
            PostEntity post = postRepository.findById(complaint.getPost().getUuid()).orElseThrow(PostNotFoundException::new);

            if (complaint.getComment() != null){
                CommentEntity comentario = commentRepository.findById(complaint.getComment().getUuid()).orElseThrow(RuntimeException::new);
                complaint.setComment(comentario);
            }

            complaint.setUser(user);
            complaint.setPost(post);

            return complaintRepository.save(complaint);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a reclamação no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar a reclamação no repository: " + e.getMessage());
        }
    }

    public ComplaintEntity update(ComplaintEntity complaint, UUID uuid) {
        try {
            complaintRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Reclamação não existe no banco"));
            complaint.setUuid(uuid);
            return complaintRepository.save(complaint);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar a reclamação no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar a reclamação no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            complaintRepository.deleteById(uuid);
            return "Reclamação deletada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar a reclamação no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar a reclamação no repository: " + e.getMessage());
        }
    }

    public ComplaintEntity findById(UUID uuid) {
        return complaintRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Reclamação não encontrada no banco"));
    }

    public List<ComplaintEntity> findAll() {
        try {
            return complaintRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar as reclamações do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar as reclamacoes: " + e.getMessage());
        }
    }
}
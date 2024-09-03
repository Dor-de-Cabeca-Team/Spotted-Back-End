package com.Rede_Social.Services;

import com.Rede_Social.Entities.CommentEntity;
import com.Rede_Social.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // save
    public CommentEntity save(CommentEntity commentEntity) {
        try {
            return commentRepository.save(commentEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new CommentEntity();
        }
    }

    // update
    public CommentEntity update(CommentEntity commentEntity, Long id) {
        try {
            commentEntity.setId(id);
            return commentRepository.save(commentEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new CommentEntity();
        }
    }

    // delete
    public String delete(Long id) {
        try {
            if (commentRepository.findById(id).isPresent()) {
                commentRepository.deleteById(id);
                return "Comment deletado com sucesso!";
            } else {
                return "Comment não encontrado";
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar Comment: " + e.getMessage());
            return "Erro ao deletar comment.";
        }
    }

    // findAll
    public List<CommentEntity> findAll() {
        try {
            return commentRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro ao retornar lista de comments: " + e.getMessage());
            return List.of();
        }
    }

    // findById
    public CommentEntity findById(Long id) {
        try {
            return commentRepository.findById(id)
                    .orElseThrow(() -> {
                        System.out.println("Comment não encontrado com o ID: " + id);
                        return new RuntimeException("Comment não encontrado");
                    });
        } catch (Exception e) {
            System.out.println("Erro ao buscar comment: " + e.getMessage());
            return new CommentEntity();
        }
    }
}

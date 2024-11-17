package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.CommentDTO;
import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.DTO.Mapper.CommentDTOMapper;
import com.Rede_Social.DTO.Mapper.PostDTOMapper;
import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.*;
import com.Rede_Social.Service.AI.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
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

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    public String save(CommentDTO comment) {
        try {
            UserEntity user = userRepository.findById(comment.getUser()).orElseThrow(UserNotFoundException::new);
            PostEntity post = postRepository.findById(comment.getPost()).orElseThrow(PostNotFoundException::new);

            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setConteudo(comment.getConteudo());
            commentEntity.setValido(geminiService.validadeAI(commentEntity.getConteudo()));
            commentEntity.setData(Instant.now());
            commentEntity.setUser(user);
            commentEntity.setPost(post);
            commentEntity.setProfileAnimal(comment.getProfileAnimal());

            commentRepository.save(commentEntity);

            return "comentário criado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o comentário no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o comentário no repository: " + e.getMessage());
        }
    }

    public String update(CommentDTO comment, UUID uuid) {
        try {
            CommentEntity existingComment = commentRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

            existingComment.setConteudo(comment.getConteudo());
            existingComment.setValido(geminiService.validadeAI(existingComment.getConteudo()));
            existingComment.setData(comment.getData());
            existingComment.setProfileAnimal(comment.getProfileAnimal());

            commentRepository.save(existingComment);

            return "comentário atualizado";
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

    public CommentDTO findById(UUID uuid) {
        CommentEntity comment = commentRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Comentário não encontrado no banco"));
        return CommentDTOMapper.toCommentDto(comment, null, likeRepository, complaintRepository);
    }

    public List<CommentDTO> findAll() {
        try {
            List<CommentEntity> comments = commentRepository.findAll();
            List<CommentDTO> commentDTOList = new ArrayList<>();
            for (CommentEntity comment : comments) {
                commentDTOList.add(CommentDTOMapper.toCommentDto(comment, null, likeRepository, complaintRepository));
            }
            return commentDTOList;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os comentários do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os comentarios: " + e.getMessage());
        }
    }

    public List<CommentDTO> findAllValidosByPost_Uuid(UUID idPost, UUID idUser) {
        try {
            List<CommentEntity> commentsValidos = commentRepository.commentsValidos(idPost);
            List<CommentDTO> commentDTOList = new ArrayList<>();
            boolean like, reported;

            for (CommentEntity comment : commentsValidos) {
                like = likeRepository.findByCommentAndUser(comment.getUuid(), idUser).isPresent();
                reported = complaintRepository.findByCommentAndUser(comment.getUuid(), idUser).isPresent();
                CommentDTO commentDTO = CommentDTOMapper.toCommentDto(comment, idUser, likeRepository, complaintRepository);
                if(like){
                    commentDTO.setLiked(true);
                }
                if(reported){
                    commentDTO.setReported(true);
                }
                commentDTOList.add(commentDTO);
            }
            return commentDTOList;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os comentarios de um post específico" + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os comentarios de um post específico" + e.getMessage());
        }
    }
}

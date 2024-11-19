package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.LikeDTO;
import com.Rede_Social.DTO.Mapper.LikeDTOMapper;
import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Repository.CommentRepository;
import com.Rede_Social.Repository.LikeRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public String save(LikeDTO like) {
        try {
            LikeEntity likeEntity = new LikeEntity();

            UserEntity user = userRepository.findById(like.idUser()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            PostEntity post = postRepository.findById(like.idPost()).orElseThrow(() -> new RuntimeException("Post não encontrado"));
            CommentEntity comment = commentRepository.findById(like.idComment()).orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

            likeEntity.setUser(user);
            likeEntity.setPost(post);
            likeEntity.setComment(comment);
            likeRepository.save(likeEntity);

            return "Like criado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o like no repository: " + e.getMessage());
        }
    }

    public String update(LikeDTO like, UUID uuid) {
        try {
            LikeEntity existingLike = likeRepository.findById(uuid)
                    .orElseThrow(() -> new RuntimeException("Like não existe no banco"));

            UserEntity user = userRepository.findById(like.idUser())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            PostEntity post = postRepository.findById(like.idPost())
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));
            CommentEntity comment = commentRepository.findById(like.idComment())
                    .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

            existingLike.setUser(user);
            existingLike.setPost(post);
            existingLike.setComment(comment);

            likeRepository.save(existingLike);

            return "Like atualizado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar o like no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            likeRepository.deleteById(uuid);
            return "Like deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar o like no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar o like no repository: " + e.getMessage());
        }
    }

    public LikeDTO findById(UUID uuid) {
        LikeEntity likeEntity = likeRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Like não encontrado no banco"));
        return LikeDTOMapper.toLikeDto(likeEntity);
    }

    public List<LikeDTO> findAll() {
        try {
            List<LikeEntity> likes = likeRepository.findAll();
            List<LikeDTO> likesDTO = new ArrayList<>();
            for(LikeEntity likeEntity : likes) {
                likesDTO.add(LikeDTOMapper.toLikeDto(likeEntity));
            }
            return likesDTO;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os likes do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os likes no repository: " + e.getMessage());
        }
    }
}

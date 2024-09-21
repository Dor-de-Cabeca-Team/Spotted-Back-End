package com.Rede_Social.Service;

import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.LikeRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.TagRepository;
import com.Rede_Social.Repository.UserRepository;
import com.Rede_Social.Service.AI.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private LikeRepository likeRepository;

    public PostEntity save(PostEntity post) {
        try {
            UserEntity user = userRepository.findById(post.getUser().getUuid()).orElseThrow(UserNotFoundException::new);

            List<UUID> tagsId = post.getTags().stream().map(TagEntity::getUuid).toList();

            List<TagEntity> tags = tagRepository.findAllById(tagsId);

            post.setUser(user);

            post.setTags(tags);

            post.setData(Instant.now());

            post.setValido(geminiService.validadeAI(post.getConteudo()));

            return postRepository.save(post);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
        }
    }

    public PostEntity update(PostEntity post, UUID uuid) {
        try {
            postRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Post não existe no banco"));
            post.setUuid(uuid);
            return postRepository.save(post);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar o post no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar o post no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            postRepository.deleteById(uuid);
            return "Post deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar o post no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar o post no repository: " + e.getMessage());
        }
    }

    public PostEntity findById(UUID uuid) {
        return postRepository.findById(uuid).orElseThrow(PostNotFoundException::new);
    }

    public List<PostEntity> findAll() {
        try {
            return postRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os posts: " + e.getMessage());
        }
    }

    public String darLikePost(UUID idPost, UUID idUser) {
        try {
            PostEntity post = postRepository.findById(idPost).orElseThrow(PostNotFoundException::new);
            UserEntity user = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);

            LikeEntity like = new LikeEntity(UUID.randomUUID(), user, post, null);

            likeRepository.save(like);

            return "Like dado";
        } catch (PostNotFoundException | UserNotFoundException e) {
           throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao tentar dar like", e);
        }
    }
}

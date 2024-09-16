package com.Rede_Social.Service;

import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
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

    public PostEntity save(PostEntity post) {
        try {
            UserEntity user = userRepository.findById(post.getUser().getUuid()).orElseThrow(() -> new UserNotFoundException("Erro ao achar usuario"));

            List<UUID> tagsId = post.getTags().stream().map(TagEntity :: getUuid).toList();

            List<TagEntity> tags = tagRepository.findAllById(tagsId);

            post.setUser(user);

            post.setTags(tags);

            post.setData(Instant.now());

            post.setValido(geminiService.validadeAI(post.getConteudo()));

            return postRepository.save(post);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
            return new PostEntity();
        }
    }

    public PostEntity update(PostEntity post, UUID uuid) {
        try {
            postRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Post não existe no banco"));
            post.setUuid(uuid);
            return postRepository.save(post);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar o post no repository: " + e.getMessage());
            return new PostEntity();
        }
    }

    public String delete(UUID uuid) {
        try {
            postRepository.deleteById(uuid);
            return "Post deletado";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar o post no repository: " + e.getMessage());
            return "Não deu para deletar o post, erro no service";
        }
    }

    public PostEntity findById(UUID uuid) {
        return postRepository.findById(uuid).orElseThrow(() -> new PostNotFoundException());
    }

    public List<PostEntity> findAll() {
        try {
            return postRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            return List.of();
        }
    }
}

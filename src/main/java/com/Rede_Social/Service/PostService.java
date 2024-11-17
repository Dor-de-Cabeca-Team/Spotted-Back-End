package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.DTO.Mapper.PostDTOMapper;
import com.Rede_Social.Entity.*;
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

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private CommentRepository commentRepository;

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

            boolean likado = likeRepository.findByPostAndUser(idPost, idUser).isPresent();

            if(!likado) {
                LikeEntity like = new LikeEntity(UUID.randomUUID(), user, post, null);
                likeRepository.save(like);
                return "Like no Post dado";
            } else{
                throw new RuntimeException("Usuário já deu like nesse Post");
            }
        } catch (PostNotFoundException | UserNotFoundException e) {
           throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao tentar dar like no post", e);
        }
    }

    public String darLikeComentario(UUID idComentario, UUID idUser){
        try {
            CommentEntity comentario = commentRepository.findById(idComentario).orElseThrow(() -> new RuntimeException("Comentario não encontrado"));
            UserEntity user = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);

            boolean likado = likeRepository.findByCommentAndUser(idComentario, idUser).isPresent();

            if(!likado) {
                LikeEntity like = new LikeEntity(UUID.randomUUID(), user, null, comentario);
                likeRepository.save(like);
                return "Like no comentario dado";
            }else{
                throw new RuntimeException("Usuário já deu like nesse Comentário");
            }
        } catch (UserNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Erro inesperado ao tentar dar like no comentário", e);
        }
    }

    public String denunciarPost(UUID idPost, UUID idUser){
        try {
            PostEntity post = postRepository.findById(idPost).orElseThrow(PostNotFoundException::new);
            UserEntity user = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);

            ComplaintEntity denuncia = new ComplaintEntity(UUID.randomUUID(), user, post, null);

            complaintRepository.save(denuncia);

            return "Denuncia ao post feita";
        } catch (PostNotFoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Erro inesperado ao denunciar post", e);
        }
    }

    public String denunciarComentario(UUID idComentario, UUID idUser){
        try {
            CommentEntity comentario = commentRepository.findById(idComentario).orElseThrow(() -> new RuntimeException("Comentario não encontrado"));
            UserEntity user = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);

            ComplaintEntity denuncia = new ComplaintEntity(UUID.randomUUID(), user, null, comentario);

            complaintRepository.save(denuncia);

            return "Denuncia ao comentario feita";
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Erro inesperado ao denunciar comentario", e);
        }
    }

    public List<PostEntity> findByTagsNome(String tag){
        try {
            return postRepository.findByTagsNome(tag);
        } catch (Exception e){
            throw e;
        }
    }

    public List<PostEntity> Top10PostsComLike() {
        try {
            return postRepository.Top10PostsComLike();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os posts: " + e.getMessage());
        }
    }

    public PostEntity postMaisCurtidoDaSemana(){
        try{
            return postRepository.findByMaisCurtido();
        }catch (Exception e){
            throw new RuntimeException("Erro ao buscar o post mais curtido da semana: " + e.getMessage());
        }
    }

    public List<PostDTO> PostsValidos(UUID idUser) {
        try {
            List<PostEntity> postsValidos = postRepository.PostsValidos();
            List<PostDTO> postDTOList = new ArrayList<>();
            for (PostEntity post : postsValidos) {
                PostDTO postDto = PostDTOMapper.toPostDto(post, idUser, likeRepository);
                postDTOList.add(postDto);
            }
            return postDTOList;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os posts: " + e.getMessage());
        }
    }
}
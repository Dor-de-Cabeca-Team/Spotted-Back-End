package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.DTO.Consulta.Top10PostsComLike.PostConsultaTop10DTO;
import com.Rede_Social.DTO.Criacao.PostCriacaoDTO;
import com.Rede_Social.DTO.Mapper.PostDTOMapper;
import com.Rede_Social.DTO.Mapper.Top10PostsComLike.PostTop10Mapper;
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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

//    }

    public String save(PostCriacaoDTO post) {
        try {
            UserEntity usuario = userRepository.findById(post.userId()).orElseThrow(UserNotFoundException::new);

            PostEntity postEntity = new PostEntity();

            postEntity.setUser(usuario);

            postEntity.setConteudo(post.conteudo());

            postEntity.setValido(geminiService.validadeAI(post.conteudo()));

            postEntity.setData(Instant.now());

            List<TagEntity> tagEntities = post.tags().stream()
                    .map(dto -> {
                        return tagRepository.findByNome(dto.nome())
                                .orElseGet(() -> new TagEntity(dto.nome()));
                    })
                    .toList();

            postEntity.setProfileAnimal(ThreadLocalRandom.current().nextInt(1, 21));

            postEntity.setTags(tagEntities);

            postRepository.save(postEntity);

            return "Post Criado";
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
        }
    }


    public PostDTO findById(UUID uuid) {
        PostEntity post = postRepository.findById(uuid).orElseThrow(PostNotFoundException::new);
        return PostDTOMapper.toPostDto(post, null, likeRepository, complaintRepository);
    }

    public List<PostDTO> findAll() {
        try {
            List<PostEntity> posts = postRepository.findAll();
            List<PostDTO> postDTOList = new ArrayList<>();
            for (PostEntity post : posts) {
                PostDTO postDto = PostDTOMapper.toPostDto(post, null, likeRepository, complaintRepository);
                postDTOList.add(postDto);
            }
            return postDTOList;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os posts: " + e.getMessage());
        }
    }

    public String darLikePost(UUID idPost, UUID idUser) {
        try {
            PostEntity post = postRepository.findById(idPost).orElseThrow(PostNotFoundException::new);
            UserEntity user = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);

            Optional<LikeEntity> existingLike = likeRepository.findByPostAndUser(idPost, idUser);

            if (!existingLike.isPresent()) {
                LikeEntity newLike = new LikeEntity(UUID.randomUUID(), user, post, null);
                likeRepository.save(newLike);
                return "Like no Post dado";
            } else {
                likeRepository.deleteById(existingLike.get().getUuid());
                return "Like no Post removido";
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

            Optional<LikeEntity> existingLike = likeRepository.findByCommentAndUser(idComentario, idUser);

            if(!existingLike.isPresent()) {
                LikeEntity like = new LikeEntity(UUID.randomUUID(), user, null, comentario);
                likeRepository.save(like);
                return "Like no comentario dado";
            }else{
                likeRepository.deleteById(existingLike.get().getUuid());
                return "Like no Post removido";
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

            Optional<ComplaintEntity> existingComplaint = complaintRepository.findByPostAndUser(idPost, idUser);

            if (!existingComplaint.isPresent()) {
                ComplaintEntity denuncia = new ComplaintEntity(UUID.randomUUID(), user, post, null);
                complaintRepository.save(denuncia);
                return "Denuncia ao post feita";
            } else {
                complaintRepository.deleteById(existingComplaint.get().getUuid());
                return "Denuncia no Post removida";
            }
        } catch (PostNotFoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Erro inesperado ao denunciar post", e);
        }
    }

    public String denunciarComentario(UUID idComment, UUID idUser) {
        try {
            CommentEntity comment = commentRepository.findById(idComment)
                    .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
            UserEntity user = userRepository.findById(idUser)
                    .orElseThrow(UserNotFoundException::new);

            Optional<ComplaintEntity> existingComplaint = complaintRepository.findByCommentAndUser(idComment, idUser);

            if (!existingComplaint.isPresent()) {
                ComplaintEntity denuncia = new ComplaintEntity(UUID.randomUUID(), user, null, comment);
                complaintRepository.save(denuncia);
                return "Denúncia ao comentário feita";
            } else {
                complaintRepository.deleteById(existingComplaint.get().getUuid());
                return "Denúncia ao comentário removida";
            }
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao denunciar comentário", e);
        }
    }


    public List<PostDTO> findByTagsNome(String tag){
        try {
            List<PostEntity> posts = postRepository.findByTagsNome(tag);
            List<PostDTO> postsDTO = new ArrayList<>();
            for(PostEntity post : posts){
                PostDTO postDto = PostDTOMapper.toPostDto(post, null, likeRepository, complaintRepository);
                postsDTO.add(postDto);
            }
            return postsDTO;
        } catch (Exception e){
            throw e;
        }
    }

    public List<PostConsultaTop10DTO> Top10PostsComLike() {
        try {
            List<PostEntity> posts = postRepository.Top10PostsComLike();

            return posts.stream()
                    .map(PostTop10Mapper::toPostConsultaDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os posts: " + e.getMessage());
        }
    }


    public PostDTO postMaisCurtidoDaSemana(){
        try{
            PostEntity post = postRepository.findByMaisCurtido();
            return PostDTOMapper.toPostDto(post, null, likeRepository, complaintRepository);
        }catch (Exception e){
            throw new RuntimeException("Erro ao buscar o post mais curtido da semana: " + e.getMessage());
        }
    }

    public List<PostDTO> PostsValidos(UUID idUser) {
        try {
            List<PostEntity> postsValidos = postRepository.PostsValidos();
            List<PostDTO> postDTOList = new ArrayList<>();
            boolean like, reported;

            for (PostEntity post : postsValidos) {
                like = likeRepository.findByPostAndUser(post.getUuid(), idUser).isPresent();
                reported = complaintRepository.findByPostAndUser(post.getUuid(), idUser).isPresent();
                PostDTO postDto = PostDTOMapper.toPostDto(post, idUser, likeRepository, complaintRepository);
                if(like){
                    postDto.setLiked(true);
                }
                if(reported){
                    postDto.setReported(true);
                }
                postDTOList.add(postDto);
            }
            return postDTOList;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar os posts do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar os posts: " + e.getMessage());
        }
    }
}
//    public String save(PostDTO post) {
//        try {
//            UserEntity user = userRepository.findById(post.getUserId()).orElseThrow(UserNotFoundException::new);
//
//            List<UUID> tagsId = post.getTagsId();
//
//            PostEntity postEntity = new PostEntity();
//            postEntity.setConteudo(post.getConteudo());
//            postEntity.setData(Instant.now());
//            postEntity.setValido(geminiService.validadeAI(post.getConteudo()));
//            postEntity.setUser(user);
//
//            if(tagsId != null && !tagsId.isEmpty()) {
//                List<TagEntity> tags = tagRepository.findAllById(tagsId);
//                postEntity.setTags(tags);
//            }
//
//            postRepository.save(postEntity);
//
//            return "Post Criado";
//        } catch (UserNotFoundException e) {
//            throw e;
//        } catch (Exception e) {
//            System.out.println("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
//            throw new RuntimeException("Erro no service, não deu para salvar o post no repository: " + e.getMessage());
//        }
//
//    public String update(PostCriacaoDTO post, UUID uuid) {
//        try {
//            PostEntity existingPost = postRepository.findById(uuid)
//                    .orElseThrow(() -> new RuntimeException("Post não existe no banco"));
//            existingPost.setConteudo(post.conteudo());
//            existingPost.setValido(geminiService.validadeAI(post.conteudo()));
//
//            List<TagEntity> tagEntities = post.tags().stream()
//                    .map(dto -> {
//                        return tagRepository.findByNome(dto.nome())
//                                .orElseGet(() -> new TagEntity(dto.nome()));
//                    })
//                    .toList();
//            existingPost.setTags(tagEntities);
//
//            PostEntity updatedPost = postRepository.save(existingPost);
//
//            return "Post atualizado";
//        } catch (Exception e) {
//            System.out.println("Erro no service, não deu para atualizar o post no repository: " + e.getMessage());
//            throw new RuntimeException("Erro no service, não deu para atualizar o post no repository: " + e.getMessage());
//        }
//    }
//
//    public String delete(UUID uuid) {
//        try {
//            postRepository.deleteById(uuid);
//            return "Post deletado";
//        } catch (Exception e) {
//            System.out.println("Erro no service, não deu para deletar o post no repository: " + e.getMessage());
//            throw new RuntimeException("Erro no service, não deu para deletar o post no repository: " + e.getMessage());
//        }
//    }

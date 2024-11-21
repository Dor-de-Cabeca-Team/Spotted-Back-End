package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.DTO.Consulta.TagDTO;
import com.Rede_Social.DTO.Mapper.TagDTOMapper;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.LikeRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    public String save(TagDTO tag) {
        try {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setNome(tag.nome());

            if (tag.posts() != null && !tag.posts().isEmpty()) {
                List<UUID> postIds = tag.posts().stream().map(PostDTO::getId).collect(Collectors.toList());
                List<PostEntity> posts = postRepository.findAllById(postIds);
                tagEntity.setPosts(posts);
            }

            tagRepository.save(tagEntity);

            return "Tag criada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
        }
    }

    public TagDTO findById(UUID uuid) {
        try {
            TagEntity tag = tagRepository.findById(uuid)
                    .orElseThrow(() -> new RuntimeException("Tag não encontrada no banco"));
            return TagDTOMapper.toTagDto(tag, null, likeRepository, complaintRepository);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar a tag: " + e.getMessage(), e);
        }
    }

    public List<TagDTO> findAll() {
        try {
            List<TagEntity> tags = tagRepository.findAll();
            return tags.stream()
                    .map(tag -> TagDTOMapper.toTagDto(tag, null, likeRepository, complaintRepository))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar as tags: " + e.getMessage(), e);
        }
    }
}
//    public String update(TagDTO tag, UUID uuid) {
//        try {
//            TagEntity existingTag = tagRepository.findById(uuid)
//                    .orElseThrow(() -> new RuntimeException("Tag não encontrada no banco"));
//
//            existingTag.setNome(tag.nome());
//
//            if (tag.posts() != null && !tag.posts().isEmpty()) {
//                List<UUID> postIds = tag.posts().stream().map(PostDTO::getId).collect(Collectors.toList());
//                List<PostEntity> updatedPosts = postRepository.findAllById(postIds);
//                existingTag.setPosts(updatedPosts);
//            }
//
//            tagRepository.save(existingTag);
//
//            return "Tag atualizada com sucesso";
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao atualizar a tag: " + e.getMessage(), e);
//        }
//    }
//
//    public String delete(UUID uuid) {
//        try {
//            tagRepository.deleteById(uuid);
//            return "Tag deletada";
//        } catch (Exception e) {
//            System.out.println("Erro no service, não deu para deletar a tag no repository: " + e.getMessage());
//            throw new RuntimeException("Erro no service, não deu para deletar a tag no repository: " + e.getMessage());
//        }
//    }

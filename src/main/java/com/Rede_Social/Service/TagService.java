package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.TagDTO;
import com.Rede_Social.DTO.Mapper.TagDTOMapper;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    public String save(TagDTO tag) {
        try {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setNome(tag.nome());

            if (tag.idPost() != null && !tag.idPost().isEmpty()) {
                List<PostEntity> posts = postRepository.findAllById(tag.idPost());
                tagEntity.setPosts(posts);
            }

            TagEntity savedTag = tagRepository.save(tagEntity);

            return "Tag criada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
        }
    }

    public String update(TagDTO tag, UUID uuid) {
        try {
            TagEntity existingTag = tagRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Tag não existe no banco"));

            existingTag.setNome(tag.nome());

            if (tag.idPost() != null && !tag.idPost().isEmpty()) {
                List<PostEntity> updatedPosts = postRepository.findAllById(tag.idPost());
                existingTag.setPosts(updatedPosts);
            }

            TagEntity updatedTag = tagRepository.save(existingTag);

            return "Tag alterada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar a tag no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar a tag no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            tagRepository.deleteById(uuid);
            return "Tag deletada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar a tag no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar a tag no repository: " + e.getMessage());
        }
    }

    public TagDTO findById(UUID uuid) {
        TagEntity tag = tagRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Tag não encontrada no banco"));
        return TagDTOMapper.toTagDto(tag);
    }


    public List<TagDTO> findAll() {
        try {
            List<TagEntity> tags = tagRepository.findAll();
            List<TagDTO> tagDTOS = new ArrayList<>();
            for(TagEntity tag : tags){
                tagDTOS.add(TagDTOMapper.toTagDto(tag));
            }
            return tagDTOS;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar as tags do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar a tags no repository: " + e.getMessage());
        }
    }
}

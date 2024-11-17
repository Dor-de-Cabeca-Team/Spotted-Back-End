package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.TagDTO;
import com.Rede_Social.DTO.Mapper.TagDTOMapper;
import com.Rede_Social.Entity.TagEntity;
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

    public TagDTO save(TagEntity tag) {
        try {
            tagRepository.save(tag);
            return TagDTOMapper.toTagDto(tag);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
        }
    }

    public TagDTO update(TagEntity tag, UUID uuid) {
        try {
            tagRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Tag não existe no banco"));
            tag.setUuid(uuid);
            tagRepository.save(tag);
            return TagDTOMapper.toTagDto(tag);
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

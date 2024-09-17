package com.Rede_Social.Service;

import com.Rede_Social.Entity.TagEntity;
import com.Rede_Social.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagEntity save(TagEntity tag) {
        try {
            return tagRepository.save(tag);
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar a tag no repository: " + e.getMessage());
        }
    }

    public TagEntity update(TagEntity tag, UUID uuid) {
        try {
            tagRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Tag não existe no banco"));
            tag.setUuid(uuid);
            return tagRepository.save(tag);
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

    public TagEntity findById(UUID uuid) {
        return tagRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Tag não encontrada no banco"));
    }


    public List<TagEntity> findAll() {
        try {
            return tagRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar as tags do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar a tags no repository: " + e.getMessage());
        }
    }
}

package com.Rede_Social.Services;

import com.Rede_Social.Entities.TagEntity;
import com.Rede_Social.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    //save
    public TagEntity save(TagEntity tagEntity){
        try{
            return tagRepository.save(tagEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new TagEntity();
        }
    }

    //update
    public TagEntity update(TagEntity tagEntity, Long id){
        try{
            tagEntity.setId(id);
            return tagRepository.save(tagEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new TagEntity();
        }
    }

    //delete
    public String delete(Long id){
        try {
            if (tagRepository.findById(id).isPresent()){
                tagRepository.deleteById(id);
                return "Tag deletada com sucesso!";
            } else {
                return "Tag não encontrada";
            }
        }catch (Exception e){
            System.out.println("Erro ao deletar Tag: " + e.getMessage());
            return "Erro ao deletar tag.";
        }
    }

    //findall
    public List<TagEntity> findAll(){
        try {
            return tagRepository.findAll();
        } catch (Exception e){
            System.out.println("Erro ao retornar lista de tags: " + e.getMessage());
            return List.of();
        }
    }

    //findbyid
    public TagEntity findById(Long id){
        try {
            return tagRepository.findById(id).
                    orElseThrow(() -> {
                        System.out.println("Tag não encontrado com o ID: " + id);
                        return new RuntimeException("Tag não encontrado");
                    });
        } catch (Exception e) {
            System.out.println("Erro ao buscar tag: " + e.getMessage());
            return new TagEntity();
        }
    }
}

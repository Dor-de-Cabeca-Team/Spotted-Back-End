package com.Rede_Social.Services;

import com.Rede_Social.Entities.ComentarioEntity;
import com.Rede_Social.Entities.PostEntity;
import com.Rede_Social.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private PostRepository postRepository;

    //save
    public PostEntity save(PostEntity postEntity){
        try{
            return postRepository.save(postEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new PostEntity();
        }
    }

    //update
    public PostEntity update(PostEntity postEntity, Long id){
        try{
            postEntity.setId(id);
            return postRepository.save(postEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new PostEntity();
        }
    }
ComentarioEntity comentarioEntity = new ComentarioEntity();
    //delete
    public String delete(Long id){
        try {
            if (postRepository.findById(id).isPresent()){
                postRepository.deleteById(id);
                return "Post deletado com sucesso!";
            } else {
                return "Post não encontrado";
            }
        }catch (Exception e){
            System.out.println("Erro ao deletar Post: " + e.getMessage());
            return "Erro ao deletar post.";
        }
    }

    //findall
    public List<PostEntity> findAll(){
        try {
            return postRepository.findAll();
        } catch (Exception e){
            System.out.println("Erro ao retornar lista de posts: " + e.getMessage());
            return List.of();
        }
    }

    //findbyid
    public PostEntity findById(Long id){
        try {
            return postRepository.findById(id).
                    orElseThrow(() -> {
                        System.out.println("Post não encontrado com o ID: " + id);
                        return new RuntimeException("Post não encontrado");
                    });
        } catch (Exception e) {
            System.out.println("Erro ao buscar post: " + e.getMessage());
            return new PostEntity();
        }
    }
}

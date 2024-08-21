package com.Rede_Social.Services;

import com.Rede_Social.Entities.PostEntity;
import com.Rede_Social.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Executable;

public class PostService {

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


    //delete


    //findall


    //findbyid
}

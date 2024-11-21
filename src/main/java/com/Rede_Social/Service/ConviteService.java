package com.Rede_Social.Service;

import com.Rede_Social.Entity.ConviteEntity;
import com.Rede_Social.Repository.ConviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConviteService {

    @Autowired
    ConviteRepository conviteRepository;

    public String save(){
        try {
            conviteRepository.save(new ConviteEntity());
            return "Convite gerado";
        }catch (Exception e){
            throw e;
        }
    }

    public String delete(UUID uuid){
        try {
            conviteRepository.deleteById(uuid);
            return "Convite Deletado";
        }catch (Exception e){
            throw e;
        }
    }

    public ConviteEntity findById(UUID uuid){
        return conviteRepository.findById(uuid).orElseThrow();
    }

    public List<ConviteEntity> findAll(){
        try {
            return conviteRepository.findAll();
        }catch (Exception e){
            throw e;
        }
    }
}

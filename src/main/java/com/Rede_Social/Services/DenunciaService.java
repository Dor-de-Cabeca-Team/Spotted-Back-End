package com.Rede_Social.Services;

import com.Rede_Social.Entities.DenunciaEntity;
import com.Rede_Social.Repositories.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {
    @Autowired
    private DenunciaRepository denunciaRepository;

    //save
    public DenunciaEntity save(DenunciaEntity denunciaEntity){
        try{
            return denunciaRepository.save(denunciaEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new DenunciaEntity();
        }
    }

    //update
    public DenunciaEntity update(DenunciaEntity denunciaEntity, Long id){
        try{
            denunciaEntity.setId(id);
            return denunciaRepository.save(denunciaEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new DenunciaEntity();
        }
    }

    //delete
    public String delete(Long id){
        try {
            if (denunciaRepository.findById(id).isPresent()){
                denunciaRepository.deleteById(id);
                return "Denuncia deletada com sucesso!";
            } else {
                return "Denuncia não encontrada";
            }
        }catch (Exception e){
            System.out.println("Erro ao deletar Denuncia: " + e.getMessage());
            return "Erro ao deletar denuncia.";
        }
    }

    //findall
    public List<DenunciaEntity> findAll(){
        try {
            return denunciaRepository.findAll();
        } catch (Exception e){
            System.out.println("Erro ao retornar lista de denuncia: " + e.getMessage());
            return List.of();
        }
    }

    //findbyid
    public DenunciaEntity findById(Long id){
        try {
            return denunciaRepository.findById(id).
                    orElseThrow(() -> {
                        System.out.println("Denuncia não encontrada com o ID: " + id);
                        return new RuntimeException("Denuncia não encontrada");
                    });
        } catch (Exception e) {
            System.out.println("Erro ao buscar denuncia: " + e.getMessage());
            return new DenunciaEntity();
        }
    }
}

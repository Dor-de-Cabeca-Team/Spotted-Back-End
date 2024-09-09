package com.Rede_Social.Controllers;

import com.Rede_Social.Entities.DenunciaEntity;
import com.Rede_Social.Services.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @PostMapping("/save")
    public ResponseEntity<DenunciaEntity> save(@RequestBody DenunciaEntity denunciaEntity) {
        try {
            return ResponseEntity.ok(denunciaService.save(denunciaEntity));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DenunciaEntity> update(@RequestBody DenunciaEntity denunciaEntity, @PathVariable Long id){
        try {
            return ResponseEntity.ok(denunciaService.update(denunciaEntity, id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(denunciaService.delete(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DenunciaEntity>> findAll(){
        try {
            return ResponseEntity.ok(denunciaService.findAll());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<DenunciaEntity> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(denunciaService.findById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

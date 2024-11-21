package com.Rede_Social.Controller;

import com.Rede_Social.DTO.Consulta.PostDTO;
import com.Rede_Social.DTO.Consulta.Top10PostsComLike.PostConsultaTop10DTO;
import com.Rede_Social.DTO.Criacao.PostCriacaoDTO;
import com.Rede_Social.Service.CommentService;
import com.Rede_Social.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody PostCriacaoDTO post) {
        try {
            return ResponseEntity.ok(postService.save(post));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<PostDTO> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(postService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<PostDTO>> findAll() {
        try {
            return ResponseEntity.ok(postService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @PostMapping("/like-post")
    public ResponseEntity<String> darLikePost(@RequestParam UUID idPost, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.darLikePost(idPost, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @PostMapping("/like-comentario")
    public ResponseEntity<String> darLikeComentario(@RequestParam UUID idComment, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.darLikeComentario(idComment, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @PostMapping("/denunciar-post")
    public ResponseEntity<String> denunciarPost(@RequestParam UUID idPost, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.denunciarPost(idPost, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @PostMapping("/denunciar-comentario")
    public ResponseEntity<String> denunciarComentario(@RequestParam UUID idComment, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.denunciarComentario(idComment, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findTags")
    public ResponseEntity<List<PostDTO>> findPostByTag(@RequestParam String tag){
        try {
            return ResponseEntity.ok(postService.findByTagsNome(tag));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @GetMapping("/top10PostsComLike")
    public ResponseEntity<List<PostConsultaTop10DTO>> Top10PostsComLike(@RequestParam UUID idUser) {
        try {

            List<PostConsultaTop10DTO> top10Posts = postService.Top10PostsComLike(idUser);

            return ResponseEntity.ok(top10Posts);
        } catch (Exception e) {
            System.out.println("Erro no controller: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/maisCurtidoDaSemana")
    public ResponseEntity<PostDTO> postMaisCurtidoDaSemana(){
        try{
            return ResponseEntity.ok(postService.postMaisCurtidoDaSemana());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    @GetMapping("/postsValidos")
    public ResponseEntity<List<PostDTO>> PostsValidos(@RequestParam UUID idUser) {
        try {
            return ResponseEntity.ok(postService.PostsValidos(idUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestBody PostCriacaoDTO post, @RequestParam UUID uuid) {
//        try {
//            return ResponseEntity.ok(postService.update(post, uuid));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
//        try {
//            return ResponseEntity.ok(postService.delete(uuid));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

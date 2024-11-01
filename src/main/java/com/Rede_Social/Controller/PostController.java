package com.Rede_Social.Controller;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Service.CommentService;
import com.Rede_Social.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<PostEntity> save(@RequestBody PostEntity post) {
        try {
            return ResponseEntity.ok(postService.save(post));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<PostEntity> update(@RequestBody PostEntity post, @RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(postService.update(post, uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(postService.delete(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<PostEntity> findById(@RequestParam UUID uuid) {
        try {
            return ResponseEntity.ok(postService.findById(uuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PostEntity>> findAll() {
        try {
            return ResponseEntity.ok(postService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/like-post")
    public ResponseEntity<String> darLikePost(@RequestParam UUID idPost, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.darLikePost(idPost, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/like-comentario")
    public ResponseEntity<String> darLikeComentario(@RequestParam UUID idComentario, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.darLikeComentario(idComentario, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/denunciar-post")
    public ResponseEntity<String> denunciarPost(@RequestParam UUID idPost, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.denunciarPost(idPost, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/denunciar-comentario")
    public ResponseEntity<String> denunciarComentario(@RequestParam UUID idComentario, @RequestParam UUID idUser){
        try {
            return ResponseEntity.ok(postService.denunciarComentario(idComentario, idUser));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findTags")
    public ResponseEntity<List<PostEntity>> findPostByTag(@RequestParam String tag){
        try {
            return ResponseEntity.ok(postService.findByTagsNome(tag));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/top10PostsComLike")
    public ResponseEntity<List<PostEntity>> Top10PostsComLike() {
        try {
            return ResponseEntity.ok(postService.Top10PostsComLike());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/maisCurtidoDaSemana")
    public ResponseEntity<PostEntity> postMaisCurtidoDaSemana(){
        try{
            return ResponseEntity.ok(postService.postMaisCurtidoDaSemana());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

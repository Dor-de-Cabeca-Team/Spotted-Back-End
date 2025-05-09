package com.Rede_Social.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "likee")
@Table(name = "likee")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private Instant data;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"idade", "email", "senha", "ativo", "complaints", "posts", "comments", "likes"})
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    @JsonIgnoreProperties("likes")  // Evita loop infinito durante a serialização de PostEntity
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    @JsonIgnoreProperties("likes")  // Evita loop infinito durante a serialização de CommentEntity
    private CommentEntity comment;
}

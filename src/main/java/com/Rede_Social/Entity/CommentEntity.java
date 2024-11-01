package com.Rede_Social.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comment")
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private Instant data;

    @NotBlank @NotNull @NotEmpty
    private String conteudo;

    private boolean valido;

    private Integer profileAnimal;

    @PrePersist
    private void assignRandomProfileAnimal() {
        this.profileAnimal = ThreadLocalRandom.current().nextInt(1, 21);
    }

    @OneToMany(mappedBy = "comment")
    @JsonIgnoreProperties("comment")  // Evita loop infinito durante a serialização de ComplaintEntity
    private List<ComplaintEntity> complaints = new ArrayList<>();

    @OneToMany(mappedBy = "comment")
    @JsonIgnoreProperties("comment")  // Evita loop infinito durante a serialização de LikeEntity
    private List<LikeEntity> likes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"comments", "posts", "likes", "complaints"})
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"user", "tags","likes", "complaints", "comments"})
    private PostEntity post;
}

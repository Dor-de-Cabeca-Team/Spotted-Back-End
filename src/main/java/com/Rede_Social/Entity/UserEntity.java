package com.Rede_Social.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column
    @NotNull
    @Size(min = 2, max = 100, message = "O nome n pode ter menos que 2 caracteres e mais que 100")
    private String nome;

    @Column
    @NotNull
    @Min(value = 18, message = "O usuário deve ter pelo menos 18 anos")
    private int idade;

    @Column
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @Column
    @NotNull
    @NotEmpty
    private String senha;

    Boolean ativo = false;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "complaints"})  // Evita loop infinito durante a serialização de ComplaintEntity
    private List<ComplaintEntity> complaints = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "likes"})  // Evita loop infinito durante a serialização de LikeEntity
    private List<LikeEntity> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "posts"})
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "comments"})
    private List<CommentEntity> comments = new ArrayList<>();

}
package com.Rede_Social.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tag")
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotNull
    private String nome;

    @ManyToMany
    @JsonIgnore
    private List<PostEntity> posts = new ArrayList<>();

    public TagEntity(String nome) {
        this.nome = nome;
    }
}
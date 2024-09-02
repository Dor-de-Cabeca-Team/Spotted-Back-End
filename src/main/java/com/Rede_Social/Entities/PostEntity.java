package com.Rede_Social.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post")
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant data;

    @NotBlank@NotNull@NotEmpty
    private String conteudo; // mudei o nome de descrição para conteudo

    private boolean valido; // mudei de apagado para válido

    // associações com:
    // Denuncia
    // Usuario
    // Like
}

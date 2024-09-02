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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank@NotNull@NotEmpty
    private Instant data;

    @NotBlank@NotNull@NotEmpty
    private String conteudo; // mudei o nome de descrição para conteudo

    @NotBlank@NotNull@NotEmpty
    private boolean valido; // mudei de apagado para válido

    // associações com:
    // Denuncia
    // Usuario
    // Like
}

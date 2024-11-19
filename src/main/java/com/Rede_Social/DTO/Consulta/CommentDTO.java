package com.Rede_Social.DTO.Consulta;

import com.Rede_Social.Entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private UUID id;
    private Instant data;
    private boolean liked;
    private boolean reported;
    private String conteudo;
    private int likeCount;
    private Integer profileAnimal;
    private UUID post;
}

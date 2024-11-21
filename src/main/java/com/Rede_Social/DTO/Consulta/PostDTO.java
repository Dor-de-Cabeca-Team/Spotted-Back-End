package com.Rede_Social.DTO.Consulta;

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
public class PostDTO {
    private UUID id;
    private String conteudo;
    private boolean liked;
    private boolean reported;
    private int likeCount;
    private Instant data;
    private Integer profileAnimal;
    private List<TagDTO> tags;
    private List<CommentDTO> comments;
}

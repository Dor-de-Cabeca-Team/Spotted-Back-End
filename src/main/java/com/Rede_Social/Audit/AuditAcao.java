package com.Rede_Social.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AuditAcao {
    POST("Post"),
    COMENTARIO("Comentário"),
    LIKE("Like"),
    DENUNCIA("Denúncia"),
    LOGIN("Login"),
    REGISTRO("Registro");

    public String descricao;
}

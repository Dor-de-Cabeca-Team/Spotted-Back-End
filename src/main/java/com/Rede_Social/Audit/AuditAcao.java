package com.Rede_Social.Audit;

public enum AuditAcao {
    POST("Post"),
    COMENTARIO("Comentário"),
    LIKE("Like"),
    DENUNCIA("Denúncia"),
    LOGIN("Login"),
    REGISTRO("Registro");

    private final String descricao;

    AuditAcao(String descricao) {
        this.descricao = descricao;
    }

    public String getAuditAcao() {
        return descricao;
    }
}

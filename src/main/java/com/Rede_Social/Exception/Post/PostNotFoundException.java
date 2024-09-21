package com.Rede_Social.Exception.Post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("Post nao encontrado");
    }

}

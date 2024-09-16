package com.Rede_Social.Exception.User;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Usuario nao encontrado");
    }

    public UserNotFoundException(String mensagem) {
        super(mensagem);
    }

}

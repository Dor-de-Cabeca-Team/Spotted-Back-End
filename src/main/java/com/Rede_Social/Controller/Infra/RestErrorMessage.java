package com.Rede_Social.Controller.Infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class RestErrorMessage {

    private HttpStatus status;
    private String mensagem;

}

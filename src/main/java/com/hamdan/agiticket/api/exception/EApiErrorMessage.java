package com.hamdan.agiticket.api.exception;

public enum EApiErrorMessage {

    INVALID_TOKEN("Token inválido ou expirado."),
    INCORRECT_PASSWORD("Senha incorreta."),
    UNEXPECTED_ERR("Ocorreu um erro inesperado no servidor.");

    public final String MESSAGE;

    EApiErrorMessage(String msg) {
        MESSAGE = msg;
    }

}

package com.hamdan.agiticket.api.exception;

public enum EApiErrorMessage {

    INVALID_TOKEN("Token inválido ou expirado."),
    INCORRECT_PASSWORD("Senha incorreta.");

    public final String MESSAGE;

    EApiErrorMessage(String msg) {
        MESSAGE = msg;
    }

}

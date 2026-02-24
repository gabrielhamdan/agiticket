package com.hamdan.agiticket.api.misc;

import org.springframework.http.HttpStatus;

public record ApiResponseDto(Integer status, String message) {

    public ApiResponseDto(HttpStatus httpStatus, String message) {
        this(httpStatus.value(), message);
    }

}

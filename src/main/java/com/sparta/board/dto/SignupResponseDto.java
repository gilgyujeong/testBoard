package com.sparta.board.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final String msg;

    private final int statusCode;

    public SignupResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}

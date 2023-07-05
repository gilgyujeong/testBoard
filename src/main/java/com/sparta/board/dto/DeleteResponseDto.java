package com.sparta.board.dto;

import lombok.Getter;

@Getter
public class DeleteResponseDto {
    private final String msg;
    private final int statusCode;

    public DeleteResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}

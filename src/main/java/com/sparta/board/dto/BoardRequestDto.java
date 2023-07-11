package com.sparta.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String contents;
}
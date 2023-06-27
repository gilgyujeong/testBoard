package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String writer;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.writer = board.getWriter();
    }

    public BoardResponseDto(Long id, String title, String contents, String writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;

    }
}
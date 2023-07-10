package com.sparta.board.dto;

import com.sparta.board.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String msg;
    private int status;
    private String comment;

    public CommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
    }

    public CommentResponseDto(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }
}

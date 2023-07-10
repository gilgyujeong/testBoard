package com.sparta.board.dto;

import com.sparta.board.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String comment;

    public CommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
    }
}

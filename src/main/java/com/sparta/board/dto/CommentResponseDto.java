package com.sparta.board.dto;

import com.sparta.board.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private String comment;
    private String username;
    private LocalDateTime createdAt;


    public CommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
        this.username = comment.getUsername();
        this.createdAt = comment.getCreatedAt();
    }
}

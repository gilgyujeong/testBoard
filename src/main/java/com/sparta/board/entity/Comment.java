package com.sparta.board.entity;

import com.sparta.board.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Comment(User user, CommentRequestDto commentRequestDto, Board board) {
        this.user = user;
        this.comment = commentRequestDto.getComment();
        this.board = board;
        board.getComments().add(this);
    }

    public void updateCommnet(String comment) {
        this.comment = comment;
    }
}

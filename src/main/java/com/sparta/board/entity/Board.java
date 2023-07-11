package com.sparta.board.entity;

import com.sparta.board.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity// JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Table(name = "board")
@NoArgsConstructor // 기본 생성자
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    public Board(BoardRequestDto boardRequestDto, String username) {
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.username = username;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
    }
}
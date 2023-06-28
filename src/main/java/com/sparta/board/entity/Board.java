package com.sparta.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.board.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity// JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "board")
@NoArgsConstructor // 기본 생성자
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "writer", nullable = false, length = 20)
    private String writer;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    public Board(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.writer = boardRequestDto.getWriter();
        this.contents = boardRequestDto.getContents();
        this.password = boardRequestDto.getPassword();
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.writer = boardRequestDto.getWriter();
        this.contents = boardRequestDto.getContents();
    }
}
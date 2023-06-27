package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final JdbcTemplate jdbcTemplate;

    public BoardController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/board")
    public BoardResponseDto createMemo(@RequestBody BoardRequestDto requestDto) {
        // RequestDto -> Entity
        Board board = new Board(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO board (title, contents, writer) VALUES (?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, board.getTitle());
                    preparedStatement.setString(2, board.getContents());
                    preparedStatement.setString(3, board.getWriter());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        board.setId(id);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    @GetMapping("/board")
    public List<BoardResponseDto> getBoard() {
        // DB 조회
        String sql = "SELECT * FROM board";

        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Board 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String contents = rs.getString("contents");
                String writer = rs.getString("writer");
                return new BoardResponseDto(id, title, contents, writer);
            }
        });
    }

    @PutMapping("/board/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);
        if(board != null) {
            // memo 내용 수정
            String sql = "UPDATE board SET title = ?, contents = ?, writer = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getContents(), requestDto.getWriter(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/board/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);
        if(board != null) {
            // board 삭제
            String sql = "DELETE FROM board WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    private Board findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Board board = new Board();
                board.setTitle(resultSet.getString("title"));
                board.setContents(resultSet.getString("contents"));
                board.setWriter(resultSet.getString("writer"));
                return board;
            } else {
                return null;
            }
        }, id);
    }
}
package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        // RequestDto -> Entity
        Board board = new Board(requestDto);

        // DB 저장
        Board saveBoard = boardRepository.save(board);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

        return boardResponseDto;
    }

    public List<BoardResponseDto> getBoard() {
        // DB 조회
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    @Transactional
    public Long updateBoard(Long id, BoardRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findBoard(id);
        // board 내용 수정
        if (requestDto.getPassword().equals(board.getPassword())) {
            board.update(requestDto);
        } else {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        return id;
    }

    public Long deleteBoard(Long id, BoardRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findBoard(id);
        // board 삭제
        if (requestDto.getPassword().equals(board.getPassword())) {
            boardRepository.delete(board);
        } else {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        return id;
    }

    public BoardResponseDto getBoardOne(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board); // 이동은 Dto 로!!!
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 값이 존재하지 않습니다.")
        );
    }
}
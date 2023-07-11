package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.DeleteResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Board board = new Board(requestDto, user.getUsername());
        // DB 저장
        Board saveBoard = boardRepository.save(board);
        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

        return boardResponseDto;
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board); // 이동은 Dto 로!!!
    }

    public List<BoardResponseDto> getBoardList() {
        // DB 조회
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findBoard(id);

        // 동일한 username 인지 확인
        if (board.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            // board 내용 수정
            board.update(requestDto);
            return new BoardResponseDto(board);
        } else {
            throw new IllegalArgumentException("사용자 정보가 다릅니다.");
        }
    }

    public DeleteResponseDto deleteBoard(Long id, User user) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findBoard(id);
        // 동일한 username 인지 확인
        if (board.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            // board 삭제
            boardRepository.delete(board);
            return new DeleteResponseDto("게시글 삭제 성공", HttpStatus.OK.value());
        } else {
            throw new IllegalArgumentException("사용자 정보가 다릅니다.");
        }
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 값이 존재하지 않습니다.")
        );
    }
}
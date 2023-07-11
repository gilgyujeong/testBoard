package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.DeleteResponseDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 작성
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDto, userDetails.getUser());
    }

    // 게시글 전체 조회
    @GetMapping("/board")
    public List<BoardResponseDto> getBoard() {
        return boardService.getBoardList();
    }

    // 선택한 게시글 조회
    @GetMapping("/board/{id}")
    public BoardResponseDto getBoardOne(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 게시글 수정
    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id,
                                        @RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    public DeleteResponseDto deleteBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id, userDetails.getUser());
    }
}
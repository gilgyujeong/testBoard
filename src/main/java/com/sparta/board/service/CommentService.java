package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = new Comment();

        comment.setBoard(boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        ));
        comment.setUser(user);
        comment.setUsername(user.getUsername());
        comment.setComment(commentRequestDto.getComment());
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getCommentList(Long boardId) {
        return null;
    }
}

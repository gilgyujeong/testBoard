package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.DeleteResponseDto;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(commentId);

        if (!comment.getUsername().equals(user.getUsername()))
            return new CommentResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value());

        comment.setComment(commentRequestDto.getComment());
        return new CommentResponseDto(comment);
    }

    public DeleteResponseDto deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        // 토큰으로 확인된 사용자가 작성자인지 확인
        if (!comment.getUsername().equals(user.getUsername()))
            return new DeleteResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value());

        commentRepository.delete(comment);
        return new DeleteResponseDto("삭제 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
    }

}

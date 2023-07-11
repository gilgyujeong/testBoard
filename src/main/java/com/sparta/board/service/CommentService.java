package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.DeleteResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );
        Comment comment = new Comment(user, commentRequestDto, findBoard);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(commentId);

        isWriterValidation(comment, user);


        comment.updateCommnet(commentRequestDto.getComment());
        return new CommentResponseDto(comment);
    }

    public DeleteResponseDto deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        // 토큰으로 확인된 사용자가 작성자인지 확인
        isWriterValidation(comment, user);

        commentRepository.delete(comment);
        return new DeleteResponseDto("삭제 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
    }

    private void isWriterValidation(Comment comment, User user) {
        if(!comment.getUsername().equals(user.getUsername()) || !user.getRole().equals(UserRoleEnum.ADMIN))
            throw new IllegalArgumentException("작성자가 아닙니다.");
    }
}

package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.DeleteResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest req) {
        // 쿠키에 저장된 JWT 가져오기
        System.out.println("test");
        String token = jwtUtil.getTokenFromHeader(req);
        Claims claims;
        System.out.println("token = " + token);
        // 가져온 토큰 검증
        if (StringUtils.hasText(token)) {
            System.out.println("검증");
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않는 토큰 입니다.");
            }
            // 토큰에서 가져온 유저 정보
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() ->
                    new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            // RequestDto -> Entity
            Board board = new Board(requestDto, user.getUsername());
            // DB 저장
            Board saveBoard = boardRepository.save(board);
            // Entity -> ResponseDto
            BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

            return boardResponseDto;
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    public List<BoardResponseDto> getBoard() {
        // DB 조회
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest req) {
        // 쿠키에 저장된 JWT 가져오기
        String token = jwtUtil.getTokenFromHeader(req);
        Claims claims;

        // 가져온 토큰 검증
        if (StringUtils.hasText(token)) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않는 토큰 입니다.");
            }
            // 토큰에서 가져온 유저 정보
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() ->
                            new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            // 해당 메모가 DB에 존재하는지 확인
            Board board = findBoard(id);
            // 동일한 username 인지 확인
            if (board.getUsername().equals(user.getUsername())) {
                // board 내용 수정
                board.update(requestDto);
                return new BoardResponseDto(board);
            } else {
                throw new IllegalArgumentException("사용자 정보가 다릅니다.");
            }
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    public DeleteResponseDto deleteBoard(Long id, HttpServletRequest req) {
        // 쿠키에 저장된 JWT 가져오기
        String token = jwtUtil.getTokenFromHeader(req);
        Claims claims;

        // 가져온 토큰 검증
        if (StringUtils.hasText(token)) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않는 토큰 입니다.");
            }
            // 토큰에서 가져온 유저 정보
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() ->
                    new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            // 해당 메모가 DB에 존재하는지 확인
            Board board = findBoard(id);
            // 동일한 username 인지 확인
            if (board.getUsername().equals(user.getUsername())) {
                // board 삭제
                boardRepository.delete(board);
                return new DeleteResponseDto("게시글 삭제 성공", HttpStatus.OK.value());
            } else {
                throw new IllegalArgumentException("사용자 정보가 다릅니다.");
            }
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
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
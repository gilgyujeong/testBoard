package com.sparta.board.service;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.LoginResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.dto.SignupResponseDto;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원 가입
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword()); // 패스워드를 암호화

        // username 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 있습니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user); // 데이터베이스에 user 객체 저장
        return new SignupResponseDto("회원가입 성공", HttpStatus.OK.value());
    }
    // 로그인
//    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse res) {
//        String username = requestDto.getUsername(); // 사용자명
//        String password = requestDto.getPassword(); // 비밀번호
//
//        // 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(() ->
//                new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//        // 비밀번호 확인
//        if (!passwordEncoder.matches(password, user.getPassword())) { // matches 함수를 사용하면 암호화된 비밀번호와도 비교 가능.
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // JWT 생성 및 쿠키에 저장
//        String token = jwtUtil.createToken(user.getUsername());
//        jwtUtil.addJwtToCookie(token, res);
//
//        return new LoginResponseDto("로그인 성공", HttpStatus.OK.value());
//    }
}

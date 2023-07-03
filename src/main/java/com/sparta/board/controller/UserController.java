package com.sparta.board.controller;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.LoginResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.dto.SignupResponseDto;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/auth/signup")
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return new SignupResponseDto("fail", HttpStatus.BAD_REQUEST.value()); // 오류발생시 fail 출력
        }
        return userService.signup(requestDto);
    }
    @ResponseBody
    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        try {
            return userService.login(requestDto, res);
        } catch (Exception e) {
            return new LoginResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}

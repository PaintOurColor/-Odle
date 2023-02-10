package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 로그인(유저, 관리자)
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        userService.loginUser(userLoginRequest, response);
        return "로그인 완료";
    }
}

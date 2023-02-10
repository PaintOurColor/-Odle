package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.service.UserService;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    // 유저, 관리자 로그아웃(블랙리스트)
    // 유저, 관리자 로그아웃(redis)

    // 유저, 관리자 로그아웃
    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        userService.logoutUser(token);
        return "redirect:/users/login";
    }
}

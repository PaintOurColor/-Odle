package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.service.AdminService;
import com.paintourcolor.odle.service.UserService;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AdminService adminService;

    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid UserSignupRequest signUpRequest) {
        userService.signupUser(signUpRequest);
        return "회원가입 성공";
    }

    @PostMapping("/admin-signup")
    public String signupAdmin(@RequestBody @Valid AdminSignupRequest signUpRequest) {
        adminService.signupAdmin(signUpRequest);
        return "관리자 회원가입 성공";
    }

    // 로그인(유저, 관리자)
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        userService.loginUser(userLoginRequest, response);
        return "로그인 완료";
    }

    // 유저, 관리자 로그아웃
    @PostMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        userService.logoutUser(token);
//        return "redirect:/users/login";
        return "로그아웃 완료";
    }
}

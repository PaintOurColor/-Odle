package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid UserSignupRequest signUpRequest) {
        userService.signupUser(signUpRequest);
        return "회원가입 성공";
    }


}

package com.paintourcolor.odle.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class AdminSignupRequest {
    // 닉네임 : 영문&숫자&한글 조합 4~12글자
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$")
    private String username;

    // 이메일 : 영문&숫자&_&- + @ + 영문or숫자 + . + 영문
    @Pattern(regexp = "^[A-Za-z0-9_\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z\\-]+$")
    private String email;

    // 비밀번호 : 숫자, 문자, 특수문자 포함 8~15자리
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    private String adminToken;
}

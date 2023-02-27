package com.paintourcolor.odle.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserSignupRequest {
    // 닉네임 : 영문&숫자&한글 조합 2~12글자
//    @NotBlank(message = "닉네임을 입력해 주세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$", message = "올바른 형식으로 입력해 주세요")
    private String username;

    // 이메일 : 영문&숫자&_&- + @ + 영문or숫자 + . + 영문
//    @NotBlank(message = "이메일을 입력해 주세요")
    @Pattern(regexp = "^[A-Za-z0-9_\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z\\-]+$", message = "올바른 형식으로 입력해 주세요")
    private String email;

    // 비밀번호 : 숫자, 문자, 특수문자 포함 8~15자리
//    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "올바른 형식으로 입력해 주세요")
    private String password;
}

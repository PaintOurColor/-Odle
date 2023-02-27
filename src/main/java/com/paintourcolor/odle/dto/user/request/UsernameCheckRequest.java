package com.paintourcolor.odle.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UsernameCheckRequest {
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$", message = "올바른 형식으로 입력해 주세요")
    private String username;
}
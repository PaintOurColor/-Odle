package com.paintourcolor.odle.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class EmailCodeRequest {
    @Pattern(regexp = "^[A-Za-z0-9_\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z\\-]+$", message = "올바른 형식으로 입력해 주세요")
    private String email;
    private String code;
}

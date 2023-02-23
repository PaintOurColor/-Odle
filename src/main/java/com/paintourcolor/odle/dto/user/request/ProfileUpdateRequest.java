package com.paintourcolor.odle.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {
    private String profileImage;

    // 닉네임 : 영문&숫자&한글 조합 4~12글자
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{4,12}$")
    private String username;
    private String introduction;
}

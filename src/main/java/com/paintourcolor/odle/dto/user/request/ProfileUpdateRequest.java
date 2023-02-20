package com.paintourcolor.odle.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {
    private String profileImage;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,10}$") //4~10자리의 소문자 및 숫자로 구성
    private String username;
    private String introduction;
}

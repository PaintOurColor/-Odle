package com.paintourcolor.odle.dto.user.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {
    // 닉네임 : 영문&숫자&한글 조합 2~12글자
    @NotBlank(message = "유저 이름을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$", message = "올바른 형식으로 입력해 주세요")
    private String username;

    private MultipartFile profileImage;

    private String introduction;
}

package com.paintourcolor.odle.dto.post.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
public class TagCreateRequest {
    // #로 시작  // 숫자, 영어 대소문자, 한글 가능  // 특문, 띄어쓰기 불가능
    @Pattern(regexp = "^#([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+$")
    private String tagName;

    public TagCreateRequest(String tagName) {
        this.tagName = tagName;
    }
}
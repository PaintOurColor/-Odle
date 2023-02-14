package com.paintourcolor.odle.dto.post.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TagCreateRequest {
    private String tagName;

    public TagCreateRequest(String tagName) {
        this.tagName = tagName;
    }
}
package com.paintourcolor.odle.dto.post.response;

import lombok.Getter;

@Getter
public class TagResponse {
    private final String tagName;

    public TagResponse(String tagName) {
        this.tagName = tagName;
    }
}

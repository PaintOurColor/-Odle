package com.paintourcolor.odle.dto.post.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagCreateRequest {
    private String tagList;

    public TagCreateRequest(String tagList) {
        this.tagList = tagList;
    }
}
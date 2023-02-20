package com.paintourcolor.odle.dto.post.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagUpdateRequest {
    private String tagList;

    public TagUpdateRequest(String tagList) {
        this.tagList = tagList;
    }
}

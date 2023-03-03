package com.paintourcolor.odle.dto.post.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public class PostSearchResponse {
    private final Long postId;
    private final String title;
    private final String singer;
    private final String cover;

    public PostSearchResponse(Long postId, String title, String singer, String cover) {
        this.postId = postId;
        this.title = title;
        this.singer = singer;
        this.cover = cover;
    }
}

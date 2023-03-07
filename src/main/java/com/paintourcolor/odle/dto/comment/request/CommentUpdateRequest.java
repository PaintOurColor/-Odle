package com.paintourcolor.odle.dto.comment.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentUpdateRequest {
    private String content;
}

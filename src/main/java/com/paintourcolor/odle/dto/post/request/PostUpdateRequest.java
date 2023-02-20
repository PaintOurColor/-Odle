package com.paintourcolor.odle.dto.post.request;

import com.paintourcolor.odle.entity.EmotionEnum;
import com.paintourcolor.odle.entity.OpenOrEndEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {
    private String content;
    private OpenOrEndEnum openOrEnd;
    private EmotionEnum emotion;
    private TagUpdateRequest tagUpdateRequest;
}

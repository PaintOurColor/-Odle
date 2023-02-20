package com.paintourcolor.odle.dto.post.request;

import com.paintourcolor.odle.entity.EmotionEnum;
import com.paintourcolor.odle.entity.OpenOrEndEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {
    private Long melonId;
    private String title;
    private String singer;
    private String cover;
    private String content;
    private OpenOrEndEnum openOrEnd;
    private EmotionEnum emotion;
    private TagCreateRequest tagCreateRequest;
}

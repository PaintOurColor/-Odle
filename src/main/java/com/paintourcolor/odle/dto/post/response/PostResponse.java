package com.paintourcolor.odle.dto.post.response;

import com.paintourcolor.odle.entity.EmotionEnum;
import com.paintourcolor.odle.entity.OpenOrEndEnum;
import com.paintourcolor.odle.entity.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponse {

    private final Long id;
    private final Long user;
    private final Long music;
    private final Long likeCount;
    private final String content;
    private final OpenOrEndEnum openOrEnd;
    private final EmotionEnum emotion;
    private final String tagList;

    public PostResponse(Post post, String tagList) {
        this.id = post.getId();
        this.user = post.getUser().getId();
        this.music = post.getMusic().getId();
        this.likeCount = post.getLikeCount();
        this.content = post.getContent();
        this.openOrEnd = post.getOpenOrEnd();
        this.emotion = post.getEmotion();
        this.tagList = tagList;
    }
}

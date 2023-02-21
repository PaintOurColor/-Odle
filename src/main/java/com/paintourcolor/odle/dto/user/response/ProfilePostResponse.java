package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.EmotionEnum;
import com.paintourcolor.odle.entity.Music;
import com.paintourcolor.odle.entity.OpenOrEndEnum;
import com.paintourcolor.odle.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProfilePostResponse {
    Long postId;
    String username;
    String musicCover;
    String musicTitle;
    String musicSinger;
    String content;
    OpenOrEndEnum openOrEnd;
    EmotionEnum emotion;
    Long likeCount;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    public ProfilePostResponse(Post post) {
        this.postId = post.getId();
        this.username = post.getUser().getUsername();
        this.musicSinger= post.getMusic().getSinger();
        this.musicTitle = post.getMusic().getTitle();
        this.musicCover = post.getMusic().getCover();
        this.content = post.getContent();
        this.openOrEnd = post.getOpenOrEnd();
        this.emotion = post.getEmotion();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}

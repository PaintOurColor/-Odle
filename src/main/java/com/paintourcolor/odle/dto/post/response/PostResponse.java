package com.paintourcolor.odle.dto.post.response;

import com.paintourcolor.odle.entity.EmotionEnum;
import com.paintourcolor.odle.entity.OpenOrEndEnum;
import com.paintourcolor.odle.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final Long userId;
    private final String username;
    private final String userProfileImage;
    private final Long musicId;
    private final String musicTitle;
    private final String musicSinger;
    private final String musicCover;
    private final Long likeCount;
    private final String content;
    private final OpenOrEndEnum openOrEnd;
    private final EmotionEnum emotion;
    private final String tagList;
    private final Long commentCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;


    public PostResponse(Post post, String tagList) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.username = post.getUser().getUsername();
        this.userProfileImage = post.getUser().getProfileImage();
        this.musicId = post.getMusic().getId();
        this.musicTitle = post.getMusic().getTitle();
        this.musicSinger = post.getMusic().getSinger();
        this.musicCover = post.getMusic().getCover();
        this.likeCount = post.getLikeCount();
        this.content = post.getContent();
        this.openOrEnd = post.getOpenOrEnd();
        this.emotion = post.getEmotion();
        this.tagList = tagList;
        this.commentCount = post.getCommentCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}

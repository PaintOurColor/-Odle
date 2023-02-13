package com.paintourcolor.odle.dto.post.request;

import com.paintourcolor.odle.entity.EmotionEnum;
import com.paintourcolor.odle.entity.Music;
import com.paintourcolor.odle.entity.OpenOrEndEnum;
import com.paintourcolor.odle.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {
    private Long id;
    private User user;
    private Music music;
    private Long likeCount;
    private String content;
    private OpenOrEndEnum openOrEnd;
    private EmotionEnum emotion;

}

package com.paintourcolor.odle.entity;

import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "musicId", nullable = false)
    private Music music;
    @Column(nullable = false)
    private Long likeCount;
    private String content;
    @Column(nullable = false)
    private OpenOrEndEnum openOrEnd;
    @Column(nullable = false)
    private EmotionEnum emotion;
    @Column(nullable = false)
    private Long commentCount;

    public void plusComment() {
        this.commentCount += 1;
    }

    public void update(PostUpdateRequest postUpdateRequest) {
        this.content = postUpdateRequest.getContent();
        this.openOrEnd = postUpdateRequest.getOpenOrEndEnum();
        this.emotion = postUpdateRequest.getEmotionEnum();
    }
}
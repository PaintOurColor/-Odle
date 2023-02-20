package com.paintourcolor.odle.entity;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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
//    @ManyToOne
//    @JoinColumn(name = "melonKoreaId", nullable = false)
//    private MelonKorea melonKorea;
    @Column(nullable = false)
    private Long likeCount;
    private String content;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenOrEndEnum openOrEnd;
    @OneToMany(mappedBy = "post")
    private Set<PostLike> postLikes = new LinkedHashSet<>();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmotionEnum emotion;
    @Column(nullable = false)
    private Long commentCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new LinkedHashSet<>();

    public void plusComment() {
        this.commentCount += 1;
    }

    public void minusComment() {
        this.commentCount -= 1;
    }

    public void update(String content, OpenOrEndEnum openOrEnd, EmotionEnum emotion) {
        this.content = content;
        this.openOrEnd = openOrEnd;
        this.emotion = emotion;
    }

    public Post(User user, Music music, String content, OpenOrEndEnum openOrEnd, EmotionEnum emotion) {
        this.user = user;
        this.music = music;
        this.content = content;
        this.openOrEnd = openOrEnd;
        this.emotion = emotion;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }

    public void plusLike(User user) {
        this.likeCount++;
        this.postLikes.add(new PostLike(user, this));
    }

    public void minusLike(User user) {
        this.likeCount--;
    }

    public Long getUserId() {return user.getId();}
    public Long getMusicId() {return music.getId();}

}
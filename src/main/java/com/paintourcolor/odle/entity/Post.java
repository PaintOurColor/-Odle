package com.paintourcolor.odle.entity;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
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
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> postLikes = new HashSet<>();
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
        if (!this.postLikes.stream().anyMatch(postLike -> postLike.getUser().equals(user))) {
            this.likeCount++;
            this.postLikes.add(new PostLike(user, this));
        }
    }

    public boolean likedBy(User user) {
        return postLikes.stream().anyMatch(postLike -> postLike.getUser().equals(user));
    }

    public void minusLike(User user) {
        if (likeCount > 0) {
            this.likeCount--;
            PostLike postLike = postLikes.stream().filter(pl -> pl.getUser().equals(user)).findFirst().orElse(null);
            if (postLike != null) {
                postLikes.remove(postLike);
            }
        } else {
            this.likeCount = 0L;
        }
    }
    public Long getUserId() {return user.getId();}
    public Long getMusicId() {return music.getId();}
    public Long getLikeCount() {
        return likeCount;
    }

}
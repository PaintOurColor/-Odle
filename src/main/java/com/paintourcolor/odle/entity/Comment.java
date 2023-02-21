package com.paintourcolor.odle.entity;

import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Long likeCount;
    @Column(nullable = false)
    private Long parentId;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentLike> commentLikes = new HashSet<>();

    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.likeCount = 0L;
        this.parentId = 0L;
    }

    public void updateComment(CommentUpdateRequest commentUpdateRequest) {
        this.content = commentUpdateRequest.getContent();
    }

    public void plusLikeComment(User user) {
        this.likeCount++;
    }

    public void minusLikeComment(User user) {
        if (likeCount > 0) { // 현재 좋아요 수가 0보다 큰 경우에만 감소
            this.likeCount--;
        }
    }

    public Long getLikeCount() {
        return likeCount;
    }
}
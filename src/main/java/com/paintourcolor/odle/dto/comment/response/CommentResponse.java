package com.paintourcolor.odle.dto.comment.response;

import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.entity.Comment;
import com.paintourcolor.odle.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponse {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}

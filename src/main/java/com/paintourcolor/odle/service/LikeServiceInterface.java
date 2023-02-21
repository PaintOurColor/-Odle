package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.response.CommentLikeCountResponse;
import com.paintourcolor.odle.dto.post.response.PostLikeCountResponse;

public interface LikeServiceInterface {
    //     게시글 좋아요
    void likePost(Long postId, Long userId);

    // 게시글 좋아요 취소
    void unlikePost(Long postId, Long userId);

    // 댓글 좋아요
    void likeComment(Long postId, Long commentId, Long userId);

    // 댓글 좋아요 취소
    void unlikeComment(Long postId, Long commentId, Long userId);

    //댓글 좋아요 개수 조회
    CommentLikeCountResponse getCommentLikeCount(Long commentId);

    //    게시글 좋아요 개수 조회
    PostLikeCountResponse getPostLikeCount(Long postId);
}
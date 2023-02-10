package com.paintourcolor.odle.service;

public interface LikeServiceInterface {
    // 게시글 좋아요
    void likePost(Long postId, String username);
    // 게시글 좋아요 취소
    void unlikePost(Long postId, String username);
    // 댓글 좋아요
    void likeComment(Long postId, Long commentId, String username);
    // 댓글 좋아요 취소
    void unlikeComment(Long postId, Long commentId, String username);
}

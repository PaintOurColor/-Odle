package com.paintourcolor.odle.service;

public interface LikeServiceInterface {
//     게시글 좋아요
    void likePost(Long postId, Long userId);

    // 게시글 좋아요 취소
    void unlikePost(Long postId, Long userId);

//    // 게시글 좋아요 수 증가
//    void increasePostLikeCount(Long postId);
//
//    // 게시글 좋아요 수 감소
//    void decreasePostLikeCount(Long postId);
//
//    // 댓글 좋아요
//    void likeComment(Long commentId, Long userId);
//
//    // 댓글 좋아요 취소
//    void unlikeComment(Long commentId, Long userId);
//
//    // 댓글 좋아요 수 증가
//    void increaseCommentLikeCount(Long commentId);
//
//    // 댓글 좋아요 수 감소
//    void decreaseCommentLikeCount(Long commentId);
}

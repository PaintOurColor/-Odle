package com.paintourcolor.odle.service;

public class LikeService implements LikeServiceInterface{
    // 게시글 좋아요
    @Override
    public void likePost(Long postId, String username) {

    }

    // 게시글 좋아요 취소
    @Override
    public void unlikePost(Long postId, String username) {

    }

    // 댓글 좋아요
    @Override
    public void likeComment(Long postId, Long commentId, String username) {

    }

    // 댓글 좋아요 취소
    @Override
    public void unlikeComment(Long postId, Long commentId, String username) {

    }
}

package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;

public class CommentService implements CommentServiceInterface{
    // 댓글 작성
    @Override
    public void createComment(Long postId, CommentCreateRequest commentCreateRequest, String username) {

    }

    // 댓글 조회
    @Override
    public CommentResponse getComment(Long postId, int page) {
        return null;
    }

    // 댓글 수정
    @Override
    public void updateComment(Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest, String username) {

    }

    // 댓글 삭제
    @Override
    public void deleteComment(Long postId, Long commentId, String username) {

    }
}

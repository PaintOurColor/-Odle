package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;

public interface CommentServiceInterface {
    // 댓글 작성
    void createComment(Long postId, CommentCreateRequest commentCreateRequest, String username);
    // 댓글 조회
    CommentResponse getComment(Long postId, int page);
    // 댓글 수정
    void updateComment(Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest, String username);
    // 댓글 삭제
    void deleteComment(Long postId, Long commentId, String username);
    // 댓글 개수 조회
    CommentCountResponse getCommentCount(Long postId);
}

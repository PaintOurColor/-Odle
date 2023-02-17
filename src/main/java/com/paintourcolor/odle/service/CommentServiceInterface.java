package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentServiceInterface {
    // 댓글 작성
    void createComment(Long postId, CommentCreateRequest commentCreateRequest, String username);
    // 댓글 조회
    Page<CommentResponse> getComment(Long postId, Pageable pageable);
    // 댓글 수정
    void updateComment(Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest, Long userId);
    // 댓글 삭제
    void deleteComment(Long postId, Long commentId, Long userId);
    // 댓글 개수 조회
    CommentCountResponse getCommentCount(Long postId);
}

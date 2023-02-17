package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.CommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentServiceInterface commentService;

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable Long postId,
                                @RequestBody CommentCreateRequest commentCreateRequest,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getEmail();
        commentService.createComment(postId, commentCreateRequest, username);
        return "댓글 작성 완료";
    }

    // 댓글 조회
    @GetMapping("/{postId}/comments")
    public Page<CommentResponse> getComment(@PathVariable Long postId,
                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return commentService.getComment(postId, pageable);
    }

    // 댓글 수정
    @PutMapping("{postId}/comments/{commentId}")
    public String updateComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @RequestBody CommentUpdateRequest commentUpdateRequest,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        commentService.updateComment(postId, commentId, commentUpdateRequest, userId);
        return "댓글 수정 완료";
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        commentService.deleteComment(postId, commentId, userId);
        return "댓글 삭제 완료";
    }

    // 댓글 개수 조회 -> API 추가 필요
    @GetMapping("{postId}/comment-count")
    public CommentCountResponse getCommentCount() {
        return null;
    }
}

package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentLikeCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentLikeOrUnlikeResponse;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;
import com.paintourcolor.odle.dto.security.StatusResponse;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.AdminServiceInterface;
import com.paintourcolor.odle.service.CommentServiceInterface;
import com.paintourcolor.odle.service.LikeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentServiceInterface commentService;
    private final LikeServiceInterface likeService;
    private final AdminServiceInterface adminService;

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<StatusResponse> createComment(@PathVariable Long postId,
                                                        @RequestBody CommentCreateRequest commentCreateRequest,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "댓글 작성 완료");

        String username = userDetails.getEmail();
        commentService.createComment(postId, commentCreateRequest, username);

        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 댓글 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponse>> getComment(@PathVariable Long postId,
                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponse> commentResponsePage = commentService.getComment(postId, pageable);
        return new ResponseEntity<>(commentResponsePage, HttpStatus.OK);
    }

    // 댓글 수정
    @PutMapping("{postId}/comments/{commentId}")
    public ResponseEntity<StatusResponse> updateComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @RequestBody CommentUpdateRequest commentUpdateRequest,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "댓글 수정 완료");

        Long userId = userDetails.getUserId();
        commentService.updateComment(postId, commentId, commentUpdateRequest, userId);

        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 댓글 삭제 - 관리자 전용
    @DeleteMapping("{postId}/comments/{commentId}/admin")
    public ResponseEntity<StatusResponse> deleteCommentAdmin(@PathVariable Long postId,
                                                             @PathVariable Long commentId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.deleteComment(postId, commentId, userDetails.getUser());
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(),"댓글 삭제 완료");
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<StatusResponse> deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "댓글 삭제 완료");

        Long userId = userDetails.getUserId();
        commentService.deleteComment(postId, commentId, userId);

        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 댓글 개수 조회
    @GetMapping("{postId}/comment-count")
    public ResponseEntity<CommentCountResponse> getCommentCount(@PathVariable Long postId) {
        CommentCountResponse commentCountResponse = commentService.getCommentCount(postId);
        return new ResponseEntity<>(commentCountResponse, HttpStatus.OK);
    }

    // 댓글 좋아요
    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<StatusResponse> likeComment(@PathVariable Long postId,
                              @PathVariable Long commentId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "댓글 좋아요 완료");

        Long userId = userDetails.getUserId();
        likeService.likeComment(postId, commentId, userId);

        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{postId}/comments/{commentId}/unlike")
    public ResponseEntity<StatusResponse> unLikeComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "댓글 좋아요 취소 완료");

        Long userId = userDetails.getUserId();
        likeService.unlikeComment(postId, commentId, userId);

        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 댓글 좋아요 개수 조회
    @GetMapping("/{postId}/comments/{commentId}/like-count")
    public ResponseEntity<CommentLikeCountResponse> getCommentLikeCount(@PathVariable Long postId,
                                                                        @PathVariable Long commentId) {
        CommentLikeCountResponse commentLikeCountResponse = likeService.getCommentLikeCount(postId, commentId);
        return new ResponseEntity<>(commentLikeCountResponse, HttpStatus.OK);
    }

    // 댓글 좋아요 여부 확인
    @GetMapping("/{postId}/comments/{commentId}/like-or-unlike")
    public ResponseEntity<CommentLikeOrUnlikeResponse> getCommentLikeOrUnlikeResponse(@PathVariable Long postId,
                                                                                      @PathVariable Long commentId,
                                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentLikeOrUnlikeResponse commentLikeOrUnlikeResponse
                = likeService.getCommentLikeOrUnlikeResponse(postId, commentId, userDetails.getUserId());

        return new ResponseEntity<>(commentLikeOrUnlikeResponse, HttpStatus.OK);
    }
}

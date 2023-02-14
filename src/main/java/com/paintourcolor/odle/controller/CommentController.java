package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.CommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentServiceInterface commentService;

    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable Long postId,
                                @RequestBody CommentCreateRequest commentCreateRequest,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        commentService.createComment(postId, commentCreateRequest, username);
        return "댓글 작성 완료";
    }
}

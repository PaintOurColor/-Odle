package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.post.request.*;
import com.paintourcolor.odle.dto.post.response.PostLikeCountResponse;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.LikeServiceInterface;
import com.paintourcolor.odle.service.MusicServiceInterface;
import com.paintourcolor.odle.service.PostServiceInterface;
import com.paintourcolor.odle.service.TagServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceInterface postService;
    private final TagServiceInterface tagService;
    //private final MusicServiceInterface musicService;
    private final LikeServiceInterface likeService;

    //게시글 작성
    @PostMapping
    public String createPost(@RequestBody PostCreateRequest postCreateRequest,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        postService.createPost(postCreateRequest, user);
//        tagService.createTag(postId, tagCreateRequest);
        return "게시글 작성 완료";
    }

    //게시글 목록 조회(메인피드)
    @GetMapping()
    public List<PostResponse> getPostList(Pageable pageable,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostList(pageable);
    }

    //게시글 개별 조회
    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable Long postId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPost(postId);
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public PostResponse updatePost(@PathVariable Long postId,
                                   @RequestBody PostUpdateRequest postUpdateRequest,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return postService.updatePost(postId, postUpdateRequest, username);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public String  deleteBoard(@PathVariable Long postId,
                                         @RequestBody PostDeleteRequest postDeleteRequest,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return postService.deletePost(postId, postDeleteRequest, username);
    }

    //게시글 좋아요
    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Long postId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likePost(postId, userDetails.getUserId());
        return "게시글 좋아요 완료";
    }



    //게시글 좋아요 취소
    @DeleteMapping("/{postId}/unlike")
    public String unlikePost(@PathVariable Long postId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.unlikePost(postId, userDetails.getUserId());
        return "게시글 좋아요 취소 완료";
    }

    //게시글 좋아요 개수 호출
    @GetMapping("/{postId}/like-count")
    public PostLikeCountResponse getPostLikeCount(@PathVariable Long postId){
        return likeService.getPostLikeCount(postId);
    }
}

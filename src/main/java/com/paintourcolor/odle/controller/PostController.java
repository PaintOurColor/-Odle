package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.post.request.*;
import com.paintourcolor.odle.dto.post.response.PostLikeCountResponse;
import com.paintourcolor.odle.dto.post.response.PostLikeOrUnlikeResponse;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.dto.security.StatusResponse;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.entity.UserRoleEnum;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.AdminServiceInterface;
import com.paintourcolor.odle.service.LikeServiceInterface;
import com.paintourcolor.odle.service.PostServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceInterface postService;
    private final LikeServiceInterface likeService;
    private final AdminServiceInterface adminService;

    //게시글 작성
    @PostMapping
    public ResponseEntity<StatusResponse> createPost(@RequestBody PostCreateRequest postCreateRequest,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "게시글 작성 완료");

        User user = userDetails.getUser();
        postService.createPost(postCreateRequest, user);

        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    //게시글 목록 조회(메인피드)
    @GetMapping()
    public ResponseEntity<List<PostResponse>> getPostList(Pageable pageable,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PostResponse> postResponseList = postService.getPostList(pageable);
        return new ResponseEntity<>(postResponseList, HttpStatus.OK);
    }

    //게시글 개별 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponse postResponse = postService.getPost(postId);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                   @RequestBody PostUpdateRequest postUpdateRequest,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        PostResponse postResponse = postService.updatePost(postId, postUpdateRequest, username);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<StatusResponse> deleteBoard(@PathVariable Long postId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "게시글 삭제 완료");
        String username = userDetails.getUsername();
        postService.deletePost(postId, username);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    //게시글 좋아요
    @PostMapping("/{postId}/like")
    public ResponseEntity<StatusResponse> likePost(@PathVariable Long postId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "게시글 좋아요 완료");
        likeService.likePost(postId, userDetails.getUserId());
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }



    //게시글 좋아요 취소
    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<StatusResponse> unlikePost(@PathVariable Long postId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "게시글 좋아요 취소 완료");
        likeService.unlikePost(postId, userDetails.getUserId());
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    //게시글 좋아요 개수 호출
    @GetMapping("/{postId}/like-count")
    public ResponseEntity<PostLikeCountResponse> getPostLikeCount(@PathVariable Long postId){
        PostLikeCountResponse postLikeCountResponse = likeService.getPostLikeCount(postId);
        return new ResponseEntity<>(postLikeCountResponse, HttpStatus.OK);
    }

    //게시글 좋아요 여부 확인
    @GetMapping("/{postId}/like-or-unlike")
    public ResponseEntity<PostLikeOrUnlikeResponse> getPostLikeOrUnlikeResponse(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostLikeOrUnlikeResponse postLikeOrUnlikeResponse = likeService.getPostLikeOrUnlikeResponse(postId, userDetails.getUserId());
        return new ResponseEntity<>(postLikeOrUnlikeResponse, HttpStatus.OK);
    }

    //게시글 삭제 - 관리자 전용
    @DeleteMapping("/{postId}/admin")
    public ResponseEntity<StatusResponse> deleteBoardAdmin(@PathVariable Long postId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.deletePost(postId, userDetails.getUser());
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(),"게시글 삭제 완료");
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

}

package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.post.request.*;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.security.UserDetailsImpl;
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

    //게시글 작성
    @PostMapping
    public String createPost(@RequestBody PostCreateRequest postCreateRequest,
                             @RequestBody TagCreateRequest tagCreateRequest,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        //musicService.getMelonMusic(); //cover, singer, title 반환
        Long postId = postService.createPost(postCreateRequest, username);
        tagService.createTag(postId, tagCreateRequest);
        return "게시글 작성 완료";
    }

    //게시글 목록 조회(메인피드)
    @GetMapping()
    public List<PostResponse> getPostList(Pageable pageable,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return postService.getPostList(pageable, username);
    }

    //게시글 개별 조회
    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable Long postId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return postService.getPost(postId, username);
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
}

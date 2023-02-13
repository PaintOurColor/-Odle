package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시글 목록 조회(메인피드)
    @GetMapping()
    public List<PostResponse> getPostList(int page) {
        return postService.getPostList(page);
    }

    //게시글 개별 조회
    @GetMapping("{id}")
    public PostResponse getPost(@PathVariable("id") Long id) {
        return postService.getPost(id);
    }
}

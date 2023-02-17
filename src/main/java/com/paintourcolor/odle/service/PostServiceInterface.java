package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostServiceInterface {
    // 게시글 작성
    Long createPost(PostCreateRequest postCreateRequest, String username);
    // 게시글 목록 조회
    List<PostResponse> getPostList(Pageable pageable, String username);
    // 게시글 개별 조회
    PostResponse getPost(Long postId, String username);
    // 게시글 수정
    PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username);
    // 게시글 삭제
    String deletePost(Long postId, PostDeleteRequest postDeleteRequest, String username);
}

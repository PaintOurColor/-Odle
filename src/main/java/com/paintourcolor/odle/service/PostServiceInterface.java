package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostListResponse;
import com.paintourcolor.odle.dto.post.response.PostResponse;

public interface PostServiceInterface {
    // 게시글 작성
    void createPost(PostCreateRequest postCreateRequest, String username);
    // 게시글 목록 조회
    PostListResponse getPostList(int page);
    // 게시글 개별 조회
    PostResponse getPost(Long postId);
    // 게시글 수정
    void updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username);
    // 게시글 삭제
    void deletePost(Long postId, PostDeleteRequest postDeleteRequest, String username);
}

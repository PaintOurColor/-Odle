package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostListResponse;
import com.paintourcolor.odle.dto.post.response.PostResponse;

public class PostService implements PostServiceInterface{
    // 게시글 작성
    @Override
    public void createPost(PostCreateRequest postCreateRequest, String username) {

    }

    // 게시글 전체 조회
    @Override
    public PostListResponse getPostList(int page) {
        return null;
    }

    // 게시글 개별 조회
    @Override
    public PostResponse getPost(Long postId) {
        return null;
    }

    // 게시글 수정
    @Override
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username) {

    }

    // 게시글 삭제
    @Override
    public void deletePost(Long postId, PostDeleteRequest postDeleteRequest, String username) {

    }
}

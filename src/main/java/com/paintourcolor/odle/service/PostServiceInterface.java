package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.dto.post.response.PostSearchResponse;
import com.paintourcolor.odle.dto.user.response.PostCountResponse;
import com.paintourcolor.odle.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostServiceInterface {
    // 게시글 작성
    void createPost(PostCreateRequest postCreateRequest, User user);
    // 게시글 목록 조회
    List<PostResponse> getPostList(Pageable pageable);
    // 게시글 개별 조회
    PostResponse getPost(Long postId);
    // 게시글 수정
    PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username);
    // 게시글 삭제
    void deletePost(Long postId, String username);
    // 태그로 게시글 검색하기
    List<PostSearchResponse> getPostSearchList(Pageable pageable, String tagName);
    // 검색된 게시글 수
    PostCountResponse getPostSearchCount(String tagName);
}

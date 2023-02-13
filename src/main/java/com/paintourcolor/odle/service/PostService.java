package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostListResponse;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface{

    private final PostRepository postRepository;
    // 게시글 작성
    @Override
    public void createPost(PostCreateRequest postCreateRequest, String username) {

    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getPostList(int page) {
        return postRepository.findAllByOrderByCreatedAtDesc(page).stream().map(PostResponse::new).collect(Collectors.toList());
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

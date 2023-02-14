package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostListResponse;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.entity.Post;
import com.paintourcolor.odle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
    public List<PostResponse> getPostList(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable).stream().map(PostResponse::new).collect(Collectors.toList());
    }

    // 게시글 개별 조회
    @Transactional(readOnly = true)
    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));
        return new PostResponse(post);
    }

    // 게시글 수정
    @Transactional
    @Override
    public PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));

        //username이 다르다면 수정권한x
        if (post.getUser().getUsername().equals(username)) {
            post.update(postUpdateRequest);
            return new PostResponse(post);
        }
        throw new IllegalArgumentException("작성자만 수정 가능합니다.");
    }

    // 게시글 삭제
    @Transactional
    @Override
    public String deletePost(Long postId, PostDeleteRequest postDeleteRequest, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));
        if (post.getUser().getUsername().equals(username)) {
            postRepository.deleteById(postId);
            return  "게시글 삭제 성공";
        }
        return "작성자만 삭제 가능합니다";
    }

}

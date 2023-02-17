package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.entity.Music;
import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.entity.Post;
import com.paintourcolor.odle.repository.MusicRepository;
import com.paintourcolor.odle.entity.PostTag;
import com.paintourcolor.odle.entity.Tag;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface{

    private final PostRepository postRepository;
    private final TagServiceInterface tagService;
    private final MusicRepository musicRepository;
    // 게시글 작성
    @Override
    public Long createPost(PostCreateRequest postCreateRequest, String username) {
        Post post = postRepository.save(new Post(postCreateRequest, username));
        Music music = new Music(postCreateRequest.getTitle(), postCreateRequest.getSinger(), postCreateRequest.getCover());
        music.plusEmotionCount(postCreateRequest.getEmotion());
        postRepository.save(post);
        musicRepository.save(music);
        return post.getId();

    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream().map(post -> new PostResponse(post, tagService.getTag(post.getId()))).toList();
    }

    // 게시글 개별 조회
    @Transactional(readOnly = true)
    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));
        List<TagResponse> tagResponses = tagService.getTag(postId);
        return new PostResponse(post, tagResponses);
    }

    // 게시글 수정
    @Transactional
    @Override
    public PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));
        List<TagResponse> tagResponses = tagService.getTag(postId);  // PostResponse에 tagResponses를 넣어줘야해서 우선 작성함, 구현은 아직 X
        //username이 다르다면 수정권한x
        if (post.getUser().getUsername().equals(username)) {
            post.update(postUpdateRequest);
            return new PostResponse(post, tagResponses);
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

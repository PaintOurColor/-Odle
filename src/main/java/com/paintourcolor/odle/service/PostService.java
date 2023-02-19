package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.repository.MusicRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.PostTagRepository;
import com.paintourcolor.odle.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface {

    private final PostRepository postRepository;
    private final TagServiceInterface tagService;
    private final MusicRepository musicRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final MusicServiceInterface musicService;

    // 게시글 작성
    @Override
    public void createPost(PostCreateRequest postCreateRequest, User user) {
        Long musicId = postCreateRequest.getMelonId();
        MusicResponse musicResponse = musicService.getMusic(musicId);

        // 가져온 MusicResponse 로 Music 객체 생성 후 저장
        Music music = new Music(musicResponse.getTitle(), musicResponse.getSinger(), musicResponse.getCover());
        music.plusEmotionCount(postCreateRequest.getEmotion());
        musicRepository.save(music);

        // Post 객체 생성 후 저장
        Post post = new Post(user, music, postCreateRequest.getContent(), postCreateRequest.getOpenOrEnd(), postCreateRequest.getEmotion());
        postRepository.save(post);

        // Tag 가 있을 경우 tag 작성
        if (postCreateRequest.getTagCreateRequest() != null) {
            String tagList = postCreateRequest.getTagCreateRequest().getTagList();
            String[] tagNameList = tagList.split(" ");
            for (String tagName : tagNameList) {
                Tag tag = tagRepository.findByTagName(tagName);
                if (tag != null) {
                    tag.plusTagCount();
                } else {
                    Tag tag1 = new Tag(tagName);
                    tagRepository.save(tag1);
                }
            }
        }
        // tagCreateRequest 에 새로운 태그가 하나도 없을 경우 count 가 적용이 안 됨
        // PostTag table 에 postId 와 tagId 가 저장이 안 됨
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

package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostDeleteRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface {

    private final PostRepository postRepository;
    private final TagServiceInterface tagService;
    private final MusicRepository musicRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final MusicServiceInterface musicService;
    private final MelonKoreaRepository melonKoreaRepository;

    // 게시글 작성
    @Override
    public void createPost(PostCreateRequest postCreateRequest, User user) {
        Long melonId = postCreateRequest.getMelonId();
        MusicResponse musicResponse = musicService.getMusic(melonId);

        Music music = musicRepository.findMusicByMelonKoreaId(musicResponse.getMelonId());
        if (music != null) {
            music.plusEmotionCount(postCreateRequest.getEmotion());
        }else {
            music = new Music(musicResponse.getMelonId(), musicResponse.getTitle(), musicResponse.getSinger(), musicResponse.getCover());
            music.plusEmotionCount(postCreateRequest.getEmotion());
            musicRepository.save(music);
        }

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
                    tag = new Tag(tagName);
                }
                tagRepository.save(tag);
                PostTag postTag = new PostTag(post, tag);
                postTagRepository.save(postTag);
            }
        }
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses = new ArrayList<>();

        for (Post post : posts) {
            List<PostTag> postTags   = postTagRepository.findTagIdByPostId(post.getId());
            List<Tag> tags = new ArrayList<>();

            for (PostTag postTag : postTags) {
                tags.add(postTag.getTag());
            }

            String tagList = tags.stream().map(Tag::getTagName).collect(Collectors.joining(" "));
            postResponses.add(new PostResponse(post, tagList));
        }
        return postResponses;
    }

    // 게시글 개별 조회
    @Transactional(readOnly = true)
    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));
        List<TagResponse> tagResponses = tagService.getTag(postId);
        List<PostTag> postTags   = postTagRepository.findTagIdByPostId(postId);
        List<Tag> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            tags.add(postTag.getTag());
        }//
        String tagList = tags.stream().map(Tag::getTagName).collect(Collectors.joining(" "));
        return new PostResponse(post, tagList);//
    }

    // 게시글 수정
    @Transactional
    @Override
    public PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));
        String tagResponses = tagService.getTag(postId).toString();  // PostResponse에 tagResponses를 넣어줘야해서 우선 작성함, 구현은 아직 X
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

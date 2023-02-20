package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.dto.post.request.PostUpdateRequest;
import com.paintourcolor.odle.dto.post.response.PostResponse;
import com.paintourcolor.odle.entity.*;
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
            List<Tag> tags = createTag(tagList);
            for (Tag tag : tags) {
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
            String tagList = getTag(post.getId());
            postResponses.add(new PostResponse(post, tagList));
        }
        return postResponses;
    }

    // 게시글 개별 조회
    @Transactional(readOnly = true)
    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));

        String tagList = getTag(postId);

        return new PostResponse(post, tagList);
    }

    // 게시글 수정
    @Transactional
    @Override
    public PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));

        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        // emotion 수정됐을 때 수정된 emotion -1, 추가된 emotion +1
        Music music = musicRepository.findMusicByMelonKoreaId(post.getMusic().getMelonKoreaId());
        music.minusEmotionCount(post.getEmotion());

        post.update(postUpdateRequest.getContent(), postUpdateRequest.getOpenOrEnd(), postUpdateRequest.getEmotion());
        music.plusEmotionCount(postUpdateRequest.getEmotion());

        // tagName 수정
        if (postUpdateRequest.getTagUpdateRequest() != null) {
            // 기존에 있던 태그 다 -1
            deleteTag(postId);

            // 새로 들어온 태그
            String tagList = postUpdateRequest.getTagUpdateRequest().getTagList();
            List<Tag> tags = createTag(tagList);
            for (Tag tag : tags) {
                PostTag postTag = new PostTag(post, tag);
                postTagRepository.save(postTag);
            }
        }
        // 새로 작성된 태그 가져오기
        String tagList = getTag(postId);

        return new PostResponse(post, tagList);
    }

    // 게시글 삭제
    @Transactional
    @Override
    public String deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글은 존재하지 않습니다."));

        if (!post.getUser().getUsername().equals(username)) {
            return "작성자만 삭제 가능합니다";
        }

        //emotionCount 가 0이 될 뿐 삭제는 안 됨
        Music music = musicRepository.findMusicByMelonKoreaId(post.getMusic().getMelonKoreaId());
        music.minusEmotionCount(post.getEmotion());

        // tag -1 시켜주기, 1일 경우 0으로 되고 삭제는 따로 안 됨
        deleteTag(postId);

        postRepository.deleteById(postId);
        return  "게시글 삭제 성공";
    }

    public List<Tag> createTag(String tagList) {  // 이 메소드를 만들기 전에는 한 for문 안에서 PostTag도 해결했는데 메소드를 따로 두니까 for문을 한 번 더 돌려야 함
        String[] tagNameList = tagList.split(" ");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNameList) {
            Tag tag = tagRepository.findByTagName(tagName);
            if (tag != null) {
                tag.plusTagCount();
            } else {
                tag = new Tag(tagName);
            }
            tagRepository.save(tag);
            tags.add(tag);
        }
        return tags;
    }

    public String getTag(Long postId) {
        List<PostTag> postTags   = postTagRepository.findTagIdByPostId(postId);
        List<Tag> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            tags.add(postTag.getTag());
        }
        return tags.stream().map(Tag::getTagName).collect(Collectors.joining(" "));
    }

    public void deleteTag(Long postId) {
        List<PostTag> postTags = postTagRepository.findTagIdByPostId(postId);
        for (PostTag postTag : postTags) {
            Tag tag = tagRepository.findById(postTag.getTag().getId()).get();
            tag.minusTagCount();
            postTagRepository.delete(postTag); // 수정할 때는 필요한데 삭제할 때는 필요 없음
        }
    }
}
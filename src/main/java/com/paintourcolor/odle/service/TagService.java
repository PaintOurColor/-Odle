package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.TagCreateRequest;
import com.paintourcolor.odle.dto.post.request.TagUpdateRequest;
import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.entity.Post;
import com.paintourcolor.odle.entity.PostTag;
import com.paintourcolor.odle.entity.Tag;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.PostTagRepository;
import com.paintourcolor.odle.repository.TagRepository;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TagService implements TagServiceInterface {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    // 게시글 태그 생성
    @Transactional
    @Override
    public void createTag(TagCreateRequest tagCreateRequest) {
        Tag tag = new Tag(tagCreateRequest.getTagName());
        tagRepository.save(tag);
    }

    // 게시글 태그 조회
    @Override
    public List<TagResponse> getTag(String email, Long postId) {
        postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.")
        );
        userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tag -> new TagResponse(tag.getTagName())).toList();
    }

    // 게시글 태그 수정
    @Override
    public void updateTag(Long postId, Long tagId, String username, TagUpdateRequest tagUpdateRequest) {

    }

    // 게시글 태그 삭제
    @Override
    public void deleteTag(Long postId, Long tagId, String username) {

    }
}

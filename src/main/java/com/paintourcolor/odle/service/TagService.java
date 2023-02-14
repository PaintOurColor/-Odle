package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.TagCreateRequest;
import com.paintourcolor.odle.dto.post.request.TagUpdateRequest;
import com.paintourcolor.odle.dto.post.response.TagResponse;
import com.paintourcolor.odle.entity.Tag;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.TagRepository;
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
    private final TagRepository tagRepository;

    // 게시글 태그 생성
    @Transactional
    @Override
    public void createTag(Long postId, String email, TagCreateRequest tagCreateRequest) {
        postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.")
        );

        Pattern pattern = Pattern.compile("#(\\S+)");
        Matcher matcher = pattern.matcher(tagCreateRequest.getTagName());
        List<String> tagList = new ArrayList<>();

        while (matcher.find()) {
            tagList.add(matcher.group(1));
        }

        tagRepository.save(new Tag(tagList.toString()));
    }

    // 게시글 태그 조회
    @Override
    public TagResponse getTag(Long postId) {
        return null;
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

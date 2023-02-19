package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.TagCreateRequest;
import com.paintourcolor.odle.dto.post.request.TagUpdateRequest;
import com.paintourcolor.odle.dto.post.response.TagResponse;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TagService implements TagServiceInterface {
    private final PostTagRepository postTagRepository;

    // 게시글 태그 조회
    @Override
    public List<TagResponse> getTag(Long postId) {
        List<PostTag> postTags   = postTagRepository.findTagIdByPostId(postId);
        List<Tag> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            tags.add(postTag.getTag());
        }
        return tags.stream().map(tag1 -> new TagResponse(tag1.getTagName())).toList();
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

package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.TagCreateRequest;
import com.paintourcolor.odle.dto.post.request.TagUpdateRequest;
import com.paintourcolor.odle.dto.post.response.TagResponse;

public class TagService implements TagServiceInterface {
    // 게시글 태그 생성
    @Override
    public void createTag(TagCreateRequest tagCreateRequest) {

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

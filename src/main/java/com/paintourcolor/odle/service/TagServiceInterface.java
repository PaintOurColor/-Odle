package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.TagCreateRequest;
import com.paintourcolor.odle.dto.post.request.TagUpdateRequest;
import com.paintourcolor.odle.dto.post.response.TagResponse;

public interface TagServiceInterface {
    void createTag(Long postId, String email, TagCreateRequest tagCreateRequest);
    TagResponse getTag(Long postId);
    void updateTag(Long postId, Long tagId, String username, TagUpdateRequest tagUpdateRequest);
    void deleteTag(Long postId, Long tagId, String username);
}

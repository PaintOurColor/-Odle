package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostTagRepository extends JpaRepository <PostTag, Long> {
    List<PostTag> findTagIdByPostId(Long postId);
    List<PostTag> findPostIdByTagId(Long tagId);
    @Query("select count(distinct pt.post.id) from PostTag pt where pt.tag.id = :tagId")
    Long countPostIdByTagId(Long tagId);
}
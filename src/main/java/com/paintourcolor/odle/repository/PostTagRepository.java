package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository <PostTag, Long> {
    List<PostTag> findTagIdByPostId(Long postId);
}
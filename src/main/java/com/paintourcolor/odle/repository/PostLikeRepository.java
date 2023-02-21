package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByUserIdAndPostId(Long userId, Long postId);

    PostLike findByPostId(Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
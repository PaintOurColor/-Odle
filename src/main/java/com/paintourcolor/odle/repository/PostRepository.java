package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select post from Post post WHERE post.user.id = :userId")
    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    @Query("select count(post) from Post post WHERE post.user.id = :userId")
    Long countByUserId(Long userId);
}

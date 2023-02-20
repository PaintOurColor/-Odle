package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findAllByUserId(Long userId, Pageable pageable);
    Long countByUserId(Long userId);
}

package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

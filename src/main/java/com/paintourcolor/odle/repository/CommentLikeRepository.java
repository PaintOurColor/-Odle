package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.Comment;
import com.paintourcolor.odle.entity.CommentLike;
import com.paintourcolor.odle.entity.Post;
import com.paintourcolor.odle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
//    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);
}

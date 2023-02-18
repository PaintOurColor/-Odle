package com.paintourcolor.odle.service;

import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class LikeService implements LikeServiceInterface {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;


    // 좋아요 구현
    @Override
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다")
        );

        PostLike postLike;

        if (postLikeRepository.existsByPostId(postId)) {
            postLike = postLikeRepository.findByPostId(postId);

        } else {
            postLike = new PostLike(user, post);
        }

        postLikeRepository.save(postLike);
        post.plusLike(user); // Post 객체의 좋아요 수를 증가시킴
        postRepository.save(post);
    }

    // 좋아요 취소
    @Override
    public void unlikePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다")
        );

        PostLike postLike = postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        if (postLike != null) {
            postLikeRepository.delete(postLike);
        }

        post.minusLike(user);
        postRepository.save(post);
    }


    // 댓글 좋아요 구현
    @Override
    @Transactional
    public void likeComment(Long postId, Long commentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다"));

        if (commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        CommentLike commentLike = new CommentLike(user, comment);
        commentLikeRepository.save(commentLike);

        comment.plusLikeComment(user); // Comment 객체의 좋아요 수를 증가시킴
    }

    // 댓글 좋아요 취소
    @Override
    public void unlikeComment(Long postId, Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다")
        );

        CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(user.getId(), comment.getId());
        if (commentLike != null) {
            commentLikeRepository.delete(commentLike);
            comment.minusLikeComment(user);
        }
    }
}

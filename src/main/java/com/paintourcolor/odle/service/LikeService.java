package com.paintourcolor.odle.service;

import com.paintourcolor.odle.entity.Comment;
import com.paintourcolor.odle.entity.Post;
import com.paintourcolor.odle.entity.PostLike;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.repository.CommentLikeRepository;
import com.paintourcolor.odle.repository.PostLikeRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 좋아요 구현
    @Override
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException ("해당 게시글을 찾을 수 없습니다")
        );

        PostLike postLike;

        if(postLikeRepository.existsByPostId(postId)) {
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
                ()-> new IllegalArgumentException ("해당 게시글을 찾을 수 없습니다")
        );

        PostLike postLike = postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        if (postLike != null) {
            postLikeRepository.delete(postLike);
        }

        post.minusLike(user);
        postRepository.save(post);
    }


//    // 댓글 좋아요
//    @Override
//    public void likeComment(Long postId, Long commentId, String username) {
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        if (!userOptional.isPresent()) {
//            throw new RuntimeException("User not found");
//        }
//        User user = userOptional.get();
//        Optional<Comment> commentOptional = commentLikeRepository.findById(commentId);
//        if (!commentOptional.isPresent()) {
//            throw new RuntimeException("Comment not found");
//        }
//        Comment comment = commentOptional.get();
//        comment.addLike(user);
//        commentLikeRepository.save(comment);
//    }
//
//    // 댓글 좋아요 취소
//    @Override
//    public void unlikeComment(Long postId, Long commentId, String username) {
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        if (!userOptional.isPresent()) {
//            throw new RuntimeException("User not found");
//        }
//        User user = userOptional.get();
//        Optional<Comment> commentOptional = commentLikeRepository.findById(commentId);
//        if (!commentOptional.isPresent()) {
//            throw new RuntimeException("Comment not found");
//        }
//        Comment comment = commentOptional.get();
//        comment.removeLike(user);
//        commentLikeRepository.save(comment);
//    }
//
}

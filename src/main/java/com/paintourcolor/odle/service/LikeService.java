package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.response.CommentLikeCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentLikeOrUnlikeResponse;
import com.paintourcolor.odle.dto.post.response.PostLikeCountResponse;
import com.paintourcolor.odle.dto.post.response.PostLikeOrUnlikeResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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


    // 게시글 좋아요 구현
    @Override
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다")
        );

        if (post.likedBy(user)) {
            throw new IllegalArgumentException("이미 좋아요를 누르셨습니다.");
        }

        PostLike postLike = new PostLike(user, post);
        postLikeRepository.save(postLike);

        post.plusLike(user); // likeCount 증가
        postRepository.save(post); //likeCount가 증가된 Post를 레파지토리에 반영
    }

    // 게시글 좋아요 취소
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
            post.minusLike(user);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("해당 게시글을 좋아요한 적이 없습니다.");
        }
        postRepository.save(post);// likeCount가 감소된 Post를 레파지토리에 반영
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
        commentRepository.save(comment);// likeCount가 증가된 comment를 레파지토리에 반영
    }

    // 댓글 좋아요 취소
    @Override
    @Transactional
    public void unlikeComment(Long postId, Long commentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다"));

        CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
        if (commentLike != null) {
            commentLikeRepository.delete(commentLike);
            comment.minusLikeComment(user); // Comment 객체의 좋아요 수를 감소시킴
        } else {
            throw new IllegalArgumentException("좋아요를 찾을 수 없습니다.");
        }

        commentLikeRepository.delete(commentLike);

        comment.minusLikeComment(user); // Comment 객체의 좋아요 수를 감소시킴
        commentRepository.save(comment);// likeCount가 감소된 comment를 레파지토리에 반영
    }


    // 댓글 좋아요 개수 조회
    @Override
    public CommentLikeCountResponse getCommentLikeCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        return new CommentLikeCountResponse(comment.getLikeCount());
    }


    //    게시글 좋아요 개수 조회
    @Override
    public PostLikeCountResponse getPostLikeCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return new PostLikeCountResponse(post.getLikeCount());
    }

    @Override
    public PostLikeOrUnlikeResponse getPostLikeOrUnlikeResponse(Long postId, Long userId){
        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)){
            return new PostLikeOrUnlikeResponse("like");}
        else {return new PostLikeOrUnlikeResponse("unlike");}
    }

    @Override
    public CommentLikeOrUnlikeResponse getCommentLikeOrUnlikeResponse(Long commentId, Long userId){
        if (commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)){
            return new CommentLikeOrUnlikeResponse("like");}
        else {return new CommentLikeOrUnlikeResponse("unlike");}
    }
}

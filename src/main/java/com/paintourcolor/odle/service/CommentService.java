package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;
import com.paintourcolor.odle.entity.Comment;
import com.paintourcolor.odle.entity.Post;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.repository.CommentRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceInterface{
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 작성
    @Override
    public void createComment(Long postId, CommentCreateRequest commentCreateRequest, String email) {
        String content = commentCreateRequest.getContent();

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.")
        );

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Comment comment = new Comment(post, user, content);
        post.plusComment();
        commentRepository.save(comment);
    }

    // 댓글 조회
    @Override
    public CommentResponse getComment(Long postId, int page) {
        return null;
    }

    // 댓글 수정
    @Override
    public void updateComment(Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest, String username) {

    }

    // 댓글 삭제
    @Override
    public void deleteComment(Long postId, Long commentId, String username) {

    }

    // 댓글 개수 조회
    @Override
    public CommentCountResponse getCommentCount(Long postId) {
        return null;
    }
}

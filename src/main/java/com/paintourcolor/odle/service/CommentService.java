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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceInterface{
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 작성
    @Transactional
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
    @Transactional
    @Override
    public Page<CommentResponse> getComment(Long postId, Pageable pageable) {
        postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        return comments.map(CommentResponse::new);
    }

    // 댓글 수정
    @Transactional
    @Override
    public void updateComment(Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest, Long userId) {
        postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 작성된 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (!userId.equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("댓글 작성자 본인만 수정이 가능합니다.");
        }

        comment.updateComment(commentUpdateRequest);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    @Override
    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 작성된 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (!postId.equals(comment.getPost().getId())) {
            throw new IllegalArgumentException("해당 댓글이 선택하신 게시글에 존재하지 않습니다.");
        }

        if (!userId.equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("댓글 작성자 본인만 삭제가 가능합니다.");
        }

        post.minusComment();
        commentRepository.deleteById(commentId);
        postRepository.save(post);
    }

    // 댓글 개수 조회
    @Override
    public CommentCountResponse getCommentCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        return new CommentCountResponse(post.getCommentCount());
    }
}

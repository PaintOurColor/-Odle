package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.request.CommentUpdateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentCountResponse;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.CommentRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentService commentService;

    // Fixture
    private final User user = mock(User.class);
    private final User otherUser = mock(User.class);
    private final Post post = mock(Post.class);
    private final Comment comment = mock(Comment.class);

    @Test
    @DisplayName("댓글 작성")
    void createComment() {
    // given
        // RequestDto Mock 객체 생성
        CommentCreateRequest request = CommentCreateRequest.builder()
                .content("댓글내용입니다")
                .build();

        // 예외처리
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

    // when
        commentService.createComment(post.getId(), request, user.getEmail());

    // then
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 조회")
    void getComment() {
    // given
        // 실행되는지만 확인할거라서 size만 가지고 있는 빈 페이지 객체 생성
        Pageable pageable = Pageable.ofSize(10);
        when(commentRepository.findAllByPostId(post.getId(), pageable))
                .thenReturn(Page.empty());

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        // when
        Page<CommentResponse> responses = commentService.getComment(post.getId(), pageable);

    // then
        assertThat(responses).isEmpty(); // 비어있는 페이지 객체가 잘 응답되는지

    // verify
        verify(commentRepository).findAllByPostId(post.getId(), pageable); // find가 잘 되는지 확인
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
    // given
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("수정된 댓글내용입니다")
                .build();

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

    // when
        commentService.updateComment(post.getId(), comment.getId(), request, comment.getUser().getId());

    // then
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
    // given
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(comment));
        when(comment.getPost()).thenReturn(post);
        when(comment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

    // when
        commentService.deleteComment(post.getId(), comment.getId(), user.getId());

    // then
        verify(commentRepository).deleteById(comment.getId());
        verify(postRepository).save(post);
    }

    @Test
    @DisplayName("댓글 개수 조회")
    void getCommentCount() {
    // given
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

    // when
        CommentCountResponse response = commentService.getCommentCount(post.getId());

    // then
        assertThat(response).isNotNull();
    }

    // ------------------------- 예외처리 -------------------------

    @Test
    @DisplayName("댓글을 작성하려는 게시글이 존재하지 않을 때")
    void createCommentNonexistentPost() {
    // given
        CommentCreateRequest request = CommentCreateRequest.builder()
                .content("댓글 내용입니다.")
                .build();

    // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.createComment(null, request, user.getEmail()));
    }

    @Test
    @DisplayName("댓글을 수정하려는 게시글이 존재하지 않을 때")
    void updateCommentNonexistentPost() {
    // given
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("수정된 댓글내용입니다")
                .build();

    // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.updateComment(null, comment.getId(), request, user.getId()));
    }

    // 수정 필요
    @Test
    @DisplayName("댓글 작성자가 아닌 사람이 댓글 수정 시도")
    void updateCommentNotOwner() {
    // given
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("수정된 댓글내용입니다")
                .build();

    // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.updateComment(post.getId(), comment.getId(), request, otherUser.getId()));
    }

    @Test
    @DisplayName("댓글을 삭제하려는 게시글이 존재하지 않을 때")
    void deleteCommentNonexistentPost() {
    // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.deleteComment(null, comment.getId(), user.getId()));
    }

    @Test
    @DisplayName("삭제하려는 댓글이 존재하지 않을 때")
    void deleteCommentNonexistentComment() {
    // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.deleteComment(post.getId(), null, user.getId()));
    }

    @Test
    @DisplayName("댓글 작성자 외에 다른 사람이 삭제하려 하는 경우")
    void deleteCommentNotOwner() {
    // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.deleteComment(post.getId(), comment.getId(), otherUser.getId()));
    }

    @Test
    @DisplayName("댓글개수를 조회할 게시글이 존재하지 않을 때")
    void getCommentCountNonexistentPost() {
        // when, then
        assertThrows(IllegalArgumentException.class, () ->
                commentService.getCommentCount(null));
    }
}
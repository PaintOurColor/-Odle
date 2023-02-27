package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
import com.paintourcolor.odle.dto.comment.response.CommentResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.CommentRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("댓글 작성")
    void createComment() {
    // given
        // RequestDto Mock 객체 생성
        CommentCreateRequest request = CommentCreateRequest.builder()
                .content("댓글내용입니다")
                .build();
        // user Mock 객체 생성
        User user = new User(
                "닉네임",
                passwordEncoder.encode("qwe123QWE!@#"),
                "user@user.com",
                UserRoleEnum.USER,
                ActivationEnum.ACTIVE);
        // music Mock 객체 생성
        Music music = new Music(1L, "노래제목", "가수", "커버");
        // post Mock 객체 생성
        Post post = new Post(user, music, "게시글내용", OpenOrEndEnum.END, EmotionEnum.SAD);

        // 예외처리
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByEmail(any(String.class)))
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
        // Mock 객체 생성 -> 코드 수가 길어지니까 fixture 활용해보기
        User user = new User(
                "닉네임",
                passwordEncoder.encode("qwe123QWE!@#"),
                "user@user.com",
                UserRoleEnum.USER,
                ActivationEnum.ACTIVE);
        Music music = new Music(1L, "노래제목", "가수", "커버");
        Post post = new Post(user, music, "게시글내용", OpenOrEndEnum.END, EmotionEnum.SAD);

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
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}
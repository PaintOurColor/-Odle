package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.comment.request.CommentCreateRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    // when
//        commentService.getComment(postId, pageable);
    // then
    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}
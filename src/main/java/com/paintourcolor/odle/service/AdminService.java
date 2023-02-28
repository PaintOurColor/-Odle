package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.CommentRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminServiceInterface {

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 관리자 회원가입
    @Override
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String username = adminSignupRequest.getUsername();
        String password = passwordEncoder.encode(adminSignupRequest.getPassword());
        String email = adminSignupRequest.getEmail();

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        });

        if (!adminSignupRequest.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
        }

        UserRoleEnum role = UserRoleEnum.ADMIN;
        ActivationEnum activation = ActivationEnum.ACTIVE;

        userRepository.save(new User(username, password, email, role, activation));
    }

    // 유저 활성화(관리자가)
    @Override
    public void activateUser(Long userId, User Admin, String adminPassword) {
        if (userId.equals(Admin.getId())) {
            throw new IllegalArgumentException("본인 활성화는 고객센터에 문의 바랍니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.")
        );

        user.isInactivation(); // 비활성화 상태인지 확인. 이미 활성화 상태면 예외 발생
        Admin.matchPassword(adminPassword, passwordEncoder); //관리자 비밀번호 확인 진행

        user.updateActivation(ActivationEnum.ACTIVE);
        userRepository.save(user);

    }

    // 유저 비활성화(관리자가)
    @Override
    public void inactivateUser(Long userId, User Admin, String adminPassword) {
        if (userId.equals(Admin.getId())) {
            throw new IllegalArgumentException("본인 비활성화는 설정화면에서 진행할 수 있습니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.")
        );

        user.isActivation(); // 활성화 상태인지 확인. 이미 비활성화 상태면 예외 발생
        Admin.matchPassword(adminPassword, passwordEncoder); //관리자 비밀번호 확인 진행

        user.updateActivation(ActivationEnum.INACTIVE);
        userRepository.save(user);
    }

    // 게시글 삭제(관리자)
    @Transactional
    @Override
    public void deletePost(Long postId, User Admin) {
        Admin.isAdmin();//관리자인지 권한 확인

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        post.getMusic().minusEmotionCount(post.getEmotion()); //Emotion Count 감소

        Set<PostTag> postTags = post.getPostTags();
        if (postTags != null) { // Post가 가진 Tag가 있을 때만 Tag count 감소
            for (PostTag postTag : postTags) {
                postTag.getTag().minusTagCount();
            }
        }

        postRepository.deleteById(postId);
    }

    // 게시글 삭제(관리자)
    @Transactional
    @Override
    public void deleteComment(Long postId, Long commentId, User Admin) {
        Admin.isAdmin();//관리자인지 권한 확인

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        if(!commentRepository.existsByIdAndPost_Id(commentId, postId)){
            throw new EntityNotFoundException("삭제하려는 댓글과 게시글 번호를 다시한번 확인하여 주십시오.");
        }

        commentRepository.deleteById(commentId);
        post.minusComment(); // 게시글의 댓글 개수 감소
        postRepository.save(post);

    }
}
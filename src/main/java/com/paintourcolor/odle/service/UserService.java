package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.entity.UserRoleEnum;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    // 유저 회원가입
    @Transactional
    @Override
    public void signupUser(UserSignupRequest userSignupRequest) {
        String username = userSignupRequest.getUsername();
        String password = passwordEncoder.encode(userSignupRequest.getPassword());
        String email = userSignupRequest.getEmail();

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        });

        UserRoleEnum role = UserRoleEnum.USER;

        userRepository.save(new User(username, password, email));
    }

    // 로그인 유저,관리자
    @Override
    public void loginUser(UserLoginRequest userLoginRequest) {

    }

    // 로그아웃 유저, 관리자
    @Override
    public void logoutUser(String username) {

    }

    // 유저 비활성화(유저가 본인을)
    @Override
    public void inactivateUser(String username, UserInactivateRequest userInactivateRequest) {

    }

    // 유저 전체 조회
    @Override
    public UserResponse getUser(int page) {
        return null;
    }
}

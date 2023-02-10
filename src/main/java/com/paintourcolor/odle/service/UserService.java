package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.ActivationEnum;

import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.entity.UserRoleEnum;
import com.paintourcolor.odle.repository.UserRepository;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
        ActivationEnum activation = ActivationEnum.ACTIVE;

        userRepository.save(new User(username, password, email, role, activation));
    }

    // 로그인 유저,관리자
    @Transactional
    @Override
    public void loginUser(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 email의 유저가 존재하지 않습니다.")
        );

        // 받아온 이메일에 해당하는 유저가 ACTIVE 상태인지 확인 필요
        if (user.getActivation().equals(ActivationEnum.INACTIVE)) {
            throw new IllegalArgumentException("해당 유저는 탈퇴한 회원입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getEmail(), user.getRole()));
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

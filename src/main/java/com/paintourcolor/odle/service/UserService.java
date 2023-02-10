package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.ActivationEnum;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.repository.UserRepository;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 유저 회원가입
    @Override
    public void signupUser(UserSignupRequest userSignupRequest) {

    }

    // 로그인 유저,관리자
    @Override
    public void loginUser(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 email의 유저가 존재하지 않습니다.")
        );

        // 해당 유저가 활성화 상태인지 확인하고 비활성화 상태라면 로그인 못하도록 해야됨
//        userRepository.findByActivation(ActivationEnum.ACTIVE).orElseThrow(
//                () -> new IllegalArgumentException("해당 유저는 탈퇴한 회원입니다.")
//        );

        // 로그인 시도하는 유저의 계정의 레퍼지토리에 상태가 ACTIVE가 아니라면 해당 유저는 탈퇴한...
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

package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.*;
import com.paintourcolor.odle.dto.user.response.PostCountResponse;
import com.paintourcolor.odle.dto.user.response.ProfilePostResponse;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.LogoutTokenRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.RefreshTokenRepository;
import com.paintourcolor.odle.repository.UserRepository;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final LogoutTokenRepository logoutTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;

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

    @Transactional
    @Override
    public void checkEmail(EmailCheckRequest emailCheckRequest) {
        String inputEmail = emailCheckRequest.getEmail();
        if (userRepository.existsByEmail(inputEmail)) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }

    @Transactional
    @Override
    public void checkUsername(UsernameCheckRequest usernameCheckRequest) {
        String inputUsername = usernameCheckRequest.getUsername();
        if (userRepository.existsByUsername(inputUsername)) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
    }

    // 로그인 유저,관리자
    @Transactional
    @Override
    public void loginUser(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("해당 email의 유저가 존재하지 않습니다.")
        );

        user.isActivation(); // 활성화된 유저인지 확인
        user.matchPassword(password, passwordEncoder); //비밀번호 확인

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole()); // accessToken 생성
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), user.getRole()); // refreshToken 생성

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.AUTHORIZATION_REFRESH, refreshToken);
        //우선 헤더로 반환. 추후 리프레쉬 토큰은 쿠키에 저장하는 방식으로 변경 예정

        refreshTokenRepository.save(new RefreshToken(refreshToken));
        //리프레시 레파지토리에 저장
    }


    @Transactional
    @Override
    public void reissueToken(String refreshToken, HttpServletResponse response) {

        Object role = jwtUtil.getUserInfoFromToken(refreshToken).get("auth");
        String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();

        String accessToken = jwtUtil.createAccessToken(email, role);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
    }

    // 로그아웃 유저, 관리자
    @Override
    public void logoutUser(String token) {
        LogoutToken logoutToken = new LogoutToken(token);
        logoutTokenRepository.save(logoutToken);
    }

    // 유저 비활성화(유저가 본인을)
    @Override
    public void inactivateUser(String username, UserInactivateRequest userInactivateRequest) {

    }

    // 유저 전체 조회
    @Override
    public List<UserResponse> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfilePostResponse> getProfilePosts(Long userId, Pageable pageable) {
        Page<Post> Posts = postRepository.findAllByUserId(userId, pageable);
        return Posts
                .stream()
                .map(ProfilePostResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public PostCountResponse getPostCount(Long userId){
        return new PostCountResponse(postRepository.countByUserId(userId));
    }

}

package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.jwt.TokenResponse;
import com.paintourcolor.odle.dto.jwt.TokenRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.LogoutTokenRepository;
import com.paintourcolor.odle.repository.RefreshTokenRepository;
import com.paintourcolor.odle.repository.UserRepository;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final TokenProvider tokenProvider;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutTokenRepository logoutTokenRepository;

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
    public TokenResponse loginUser(UserLoginRequest userLoginRequest, HttpServletResponse response) {
//        String email = userLoginRequest.getEmail();
//        String password = userLoginRequest.getPassword();

        // 1. Login email/pw를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequest.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenDto = jwtUtil.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        response.addHeader(jwtUtil.AUTHORIZATION_HEADER,tokenDto.getAccessToken());
        response.addHeader(jwtUtil.REFRESH_AUTHORIZATION_HEADER,tokenDto.getRefreshToken());

        // 5. 토큰 발급
        return tokenDto;


//        User user = userRepository.findByEmail(email).orElseThrow(
//                () -> new IllegalArgumentException("해당 email의 유저가 존재하지 않습니다.")
//        );
//
//        // 받아온 이메일에 해당하는 유저가 ACTIVE 상태인지 확인 필요
//        if (user.getActivation().equals(ActivationEnum.INACTIVE)) {
//            throw new IllegalArgumentException("해당 유저는 탈퇴한 회원입니다.");
//        }
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getEmail(), user.getRole()));
    }

    @Transactional
    @Override
    public TokenResponse reissue(TokenRequest tokenRequest) {
        // 1. Refresh Token 검증
        if (!jwtUtil.validateToken(tokenRequest.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtUtil.getAuthentication(tokenRequest.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponse tokenDto = jwtUtil.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
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
    public UserResponse getUser(int page) {
        return null;
    }
}

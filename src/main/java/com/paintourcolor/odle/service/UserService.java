package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.*;
import com.paintourcolor.odle.dto.user.response.PostCountResponse;
import com.paintourcolor.odle.dto.user.response.ProfilePostResponse;
import com.paintourcolor.odle.dto.user.response.UserFollowOrUnfollowResponse;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.FollowRepository;
import com.paintourcolor.odle.repository.LogoutTokenRepository;
import com.paintourcolor.odle.repository.PostRepository;
import com.paintourcolor.odle.repository.UserRepository;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import com.paintourcolor.odle.util.redis.RedisServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final LogoutTokenRepository logoutTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploaderService s3UploaderService;
    private final RedisServiceInterface redisService;
    private final JwtUtil jwtUtil;

    // 유저 회원가입
    @Transactional
    @Override
    public void signupUser(UserSignupRequest userSignupRequest) {
        String username = userSignupRequest.getUsername();
        String password = passwordEncoder.encode(userSignupRequest.getPassword());
        String email = userSignupRequest.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        ActivationEnum activation = ActivationEnum.ACTIVE;

        userRepository.save(new User(username, password, email, role, activation));
    }

    // 이메일 중복 체크
    @Transactional
    @Override
    public void checkEmail(EmailCheckRequest emailCheckRequest) {
        String inputEmail = emailCheckRequest.getEmail();
        if (userRepository.existsByEmail(inputEmail)) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }

    // 닉네임 중복 체크
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
        //Header로 반환. 반환된 토큰은 프론트에서 각각 accessToken은 LocalStorage에, refershToken은 Cookie에 저장

        redisService.saveToken(refreshToken, LocalDateTime.now());
        // Redis에 토큰(Key: refreshToken, Value: 로그인 요청 시간) 저장
    }


    @Transactional
    @Override
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtUtil.getAccessToken(request); // AccessToken
        String refreshToken = jwtUtil.getRefreshToken(request); // RefreshToken

        redisService.existsToken(refreshToken); // redis에 RefreshToken이 존재하는지 확인

        if(jwtUtil.isExpiration(accessToken)) { // Reissue 시점에 AccessToken 이 만료되지 않은 경우
            logoutTokenRepository.save(new LogoutToken(accessToken));// 블랙리스트에 추가
        }

        Object role = jwtUtil.getUserInfoFromToken(refreshToken).get("auth"); // 유저 권한 정보 가져오기
        String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject(); // 유저 이메일 정보 가져오기

        String newAccessToken = jwtUtil.createAccessToken(email, role); // 새로운 AccessToken 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken); // 새로운 AccessToken Header로 반환
    }

    // 로그아웃 유저, 관리자
    @Transactional
    @Override
    public void logoutUser(HttpServletRequest request) {
        String accessToken = jwtUtil.getAccessToken(request);
        String refreshToken = jwtUtil.getRefreshToken(request);

        logoutTokenRepository.save(new LogoutToken(accessToken));//AccessToken 블랙리스트에 추가

        redisService.existsToken(refreshToken); //Redis에 RefreshToken 있는지 확인
        redisService.deleteToken(refreshToken); //Redis에서 RefreshToken 삭제
    }

    // 유저 비활성화(유저가 본인을)
    @Override
    public void inactivateMe(User user, UserInactivateRequest userInactivateRequest) {
        user.isActivation(); // 유저가 활성화 상태인지 확인
        user.matchPassword(userInactivateRequest.getPassword(), passwordEncoder); // 가져온 비밀번호가 본인꺼 맞는지 확인
        user.updateActivation(ActivationEnum.INACTIVE);
        userRepository.save(user);
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

    // 팔로우 여부 확인
    @Transactional
    @Override
    public UserFollowOrUnfollowResponse getUserFollowOrUnfollowResponse(Long followingId, Long followerId) {
        if (followRepository.existsFollowByFollowerIdAndFollowingId(followerId, followingId)){
            return new UserFollowOrUnfollowResponse("follow");}
        else {return new UserFollowOrUnfollowResponse("unfollow");}
    }

    // 프로필 업데이트
    public String uploadProfileImage(MultipartFile file, String username) throws IOException {
        String dirName = "profile-images/" + username; // 유저별로 프로필 이미지 저장 디렉토리 생성

        // 이미지 파일 업로드
        String url = s3UploaderService.upload(file, dirName);

        // 업로드된 이미지 URL을 DB에 저장
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        user.setProfileImage(url);
        userRepository.save(user);

        return url;
    }
}

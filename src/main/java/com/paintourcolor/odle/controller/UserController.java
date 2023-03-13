package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.security.StatusResponse;
import com.paintourcolor.odle.dto.user.request.*;
import com.paintourcolor.odle.dto.user.response.*;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.*;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceInterface userService;
    private final AdminServiceInterface adminService;
    private final FollowServiceInterface followService;
    private final ProfileServiceInterface profileService;
    private final EmailServiceInterface emailService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signUp(@RequestBody @Valid UserSignupRequest signUpRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 성공");
        userService.signupUser(signUpRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    @PostMapping("/admin-signup")
    public ResponseEntity<StatusResponse> signupAdmin(@RequestBody @Valid AdminSignupRequest signUpRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "관리자 회원가입 성공");
        adminService.signupAdmin(signUpRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 회원가입 이메일 중복 확인
    @PostMapping("/check-email")
    public ResponseEntity<StatusResponse> checkEmail(@RequestBody @Valid EmailCheckRequest emailCheckRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "사용 가능한 이메일입니다.");
        userService.checkEmail(emailCheckRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 회원가입 닉네임 중복 확인
    @PostMapping("/check-username")
    public ResponseEntity<StatusResponse> checkUsername(@RequestBody @Valid UsernameCheckRequest usernameCheckRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "사용 가능한 닉네임입니다.");
        userService.checkUsername(usernameCheckRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 로그인(유저, 관리자)
    @PostMapping("/login")
    public ResponseEntity<StatusResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest,
                                                    HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그인 성공");
        userService.loginUser(userLoginRequest, response);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 유저, 관리자 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<StatusResponse> logoutUser(HttpServletRequest request) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그아웃 완료");
        userService.logoutUser(request);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // AccessToken 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenReissueResponse> reissueToken(HttpServletRequest request, HttpServletResponse response){
        return new ResponseEntity<>(userService.reissueToken(request, response),HttpStatus.CREATED);
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ로그인 및 회원가입 외 유저 기능 여기서부터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
    // 관리자가 유저 목록 조회
    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUsers(@PageableDefault(sort = "id") Pageable pageable,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<UserResponse> userList = userService.getUsers(pageable);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    // 내 프로필에서 내 게시글들 목록 조회
    @GetMapping("/{userId}/profile/posts")
    public ResponseEntity<List<ProfilePostResponse>> getProfilePosts(@PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
                                                                     @PathVariable Long userId,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<ProfilePostResponse> profilePostList = userService.getProfilePosts(userId, pageable);
        return new ResponseEntity<>(profilePostList,HttpStatus.OK);
    }

    // 프로필 페이지에서 자신의 게시글 수
    @GetMapping("/{userId}/post-count")
    public ResponseEntity<PostCountResponse> getPostCount(@PathVariable Long userId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(userService.getPostCount(userId),HttpStatus.OK);
    }

    // 본인 계정 비활성화 하기
    @PatchMapping("/inactivation")
    public ResponseEntity<StatusResponse> inactivateMe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UserInactivateRequest userInactivateRequest) {
        User user = userDetails.getUser();
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "비활성화가 완료되었습니다.");
        userService.inactivateMe(user, userInactivateRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ관리자의 전용 기능 여기서부터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
    @PatchMapping("/{userId}/activation/admin")
    public ResponseEntity<String> activateUser(@PathVariable Long userId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestBody UserActivateRequest userActivateRequest) {
        adminService.activateUser(userId, userDetails.getUser(), userActivateRequest.getPassword());
        return new ResponseEntity<>("유저 활성화 완료",HttpStatus.OK);
    }

    @PatchMapping("/{userId}/inactivation/admin")
    public ResponseEntity<String> inactivateUser(@PathVariable Long userId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestBody UserActivateRequest userActivateRequest) {
        adminService.inactivateUser(userId, userDetails.getUser(), userActivateRequest.getPassword());
        return new ResponseEntity<>("유저 비활성화 완료",HttpStatus.OK);
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ유저 프로필 기능 여기서부터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

    // 프로필 수정
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "introduction", required = false) String introduction,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .username(username)
                .introduction(introduction)
                .profileImage(profileImage)
                .build();

        profileService.updateProfile(userDetails.getUserId(), profileUpdateRequest);
        return new ResponseEntity<>("프로필 수정 완료", HttpStatus.OK);
    }


    // 다른사람의 프로필 조회
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ProfileResponse profile = profileService.getProfile(userId);
        return new ResponseEntity<>(profile,HttpStatus.OK);
    }

    // 모든 유저 간편 프로필 조회
    @GetMapping("/{userId}/profile/simple")
    public ResponseEntity<ProfileSimpleResponse> getSimpleProfile(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ProfileSimpleResponse profileSimple = profileService.getSimpleProfile(userId);
        return new ResponseEntity<>(profileSimple,HttpStatus.OK);
    }

    // 본인 간편 프로필 조회
    @GetMapping("/profile/simple")
    public ResponseEntity<MyProfileSimpleResponse> getMySimpleProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      HttpServletRequest request){
        String accessToken = jwtUtil.getAccessToken(request);
        MyProfileSimpleResponse profileSimple = profileService.getMySimpleProfile(userDetails.getUser(), accessToken);
        return new ResponseEntity<>(profileSimple,HttpStatus.OK);
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ팔로우 기능 여기서부터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

    // 유저 팔로우 하기
    @PostMapping("/{userId}/follow")
    public ResponseEntity<StatusResponse> followUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.followUser(userDetails.getUserId(), userId);
        return new ResponseEntity<>(new StatusResponse(HttpStatus.OK.value(),"팔로우 완료") , HttpStatus.OK);
    }

    // 유저 팔로우 취소
    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<StatusResponse> unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.unfollowUser(userDetails.getUserId(), userId);
        return new ResponseEntity<>(new StatusResponse(HttpStatus.OK.value(),"언팔로우 완료"), HttpStatus.OK);
    }

    // 팔로워 목록 조회
    @GetMapping("/{userId}/follower")
    public ResponseEntity<List<FollowerResponse>> getFollowers(@PathVariable Long userId, Pageable pageable) {
        List<FollowerResponse> followerList = followService.getFollowers(userId, pageable);
        return new ResponseEntity<>(followerList,HttpStatus.OK);
    }

    // 팔로잉 목록 조회
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowingResponse>> getFollowings(@PathVariable Long userId, Pageable pageable) {
        List<FollowingResponse> followingList = followService.getFollowings(userId, pageable);
        return new ResponseEntity<>(followingList,HttpStatus.OK);
    }

    // 팔로워 수 조회
    @GetMapping("/{userId}/follower-count")
    public ResponseEntity<FollowerCountResponse> countFollower(@PathVariable Long userId) {
        FollowerCountResponse followerCount = followService.countFollower(userId);
        return new ResponseEntity<>(followerCount,HttpStatus.OK);
    }

    // 팔로잉 수 조회
    @GetMapping("/{userId}/following-count")
    public ResponseEntity<FollowingCountResponse> countFollowing(@PathVariable Long userId) {
        FollowingCountResponse followingCount = followService.countFollowing(userId);
        return new ResponseEntity<>(followingCount,HttpStatus.OK);
    }

    // 팔로우 여부 확인
    @GetMapping("/{userId}/follow-or-unfollow")
    public ResponseEntity<UserFollowOrUnfollowResponse> getUserFollowOrUnfollowResponse(@PathVariable Long userId,
                                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserFollowOrUnfollowResponse userFollowOrUnfollowResponse = userService.getUserFollowOrUnfollowResponse(userId, userDetails.getUserId());
        return new ResponseEntity<>(userFollowOrUnfollowResponse, HttpStatus.OK);
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ이메일 인증 여기부터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

    // 인증 이메일 발송
    @PostMapping("/email")
    public ResponseEntity<StatusResponse> sendEmailCode(@RequestBody @Valid EmailCheckRequest emailCheckRequest) throws Exception {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "인증 메일이 전송되었습니다.");
        emailService.sendEmail(emailCheckRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 이메일 인증 코드 확인
    @PostMapping("/verify-code")
    public ResponseEntity<StatusResponse> verifyEmailCode(@RequestBody @Valid EmailCodeRequest emailCodeRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "이메일 인증에 성공했습니다.");
        emailService.verifyCode(emailCodeRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}

package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.jwt.TokenResponse;
import com.paintourcolor.odle.dto.jwt.TokenRequest;
import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.response.FollowerCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowingCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowingResponse;
import com.paintourcolor.odle.security.UserDetailsImpl;
import com.paintourcolor.odle.service.*;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceInterface userService;
    private final AdminServiceInterface adminService;
    private final FollowServiceInterface followService;

    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid UserSignupRequest signUpRequest) {
        userService.signupUser(signUpRequest);
        return "회원가입 성공";
    }

    @PostMapping("/admin-signup")
    public String signupAdmin(@RequestBody @Valid AdminSignupRequest signUpRequest) {
        adminService.signupAdmin(signUpRequest);
        return "관리자 회원가입 성공";
    }

    // 로그인(유저, 관리자)
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        userService.loginUser(userLoginRequest, response);
        return "로그인 완료";
    }
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(userService.reissue(tokenRequest));
    }

    // 유저, 관리자 로그아웃
    @PostMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        userService.logoutUser(token);
//        return "redirect:/users/login";
        return "로그아웃 완료";
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.followUser(userDetails.getUserId(), userId);
        return new ResponseEntity<>("팔로우 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.unfollowUser(userDetails.getUserId(), userId);
        return new ResponseEntity<>("팔로우 취소 완료", HttpStatus.OK);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowingResponse>> getFollowings(@PathVariable Long userId, Pageable pageable) {
        List<FollowingResponse> followingList = followService.getFollowings(userId, pageable);
        return new ResponseEntity<>(followingList,HttpStatus.OK);
    }

    @GetMapping("/{userId}/follower-count")
    public ResponseEntity<FollowerCountResponse> countFollower(@PathVariable Long userId) {
        FollowerCountResponse followerCount = followService.countFollower(userId);
        return new ResponseEntity<>(followerCount,HttpStatus.OK);
    }

    @GetMapping("/{userId}/following-count")
    public ResponseEntity<FollowingCountResponse> getFollowers(@PathVariable Long userId) {
        FollowingCountResponse followingCount = followService.countFollowing(userId);
        return new ResponseEntity<>(followingCount,HttpStatus.OK);
    }

}

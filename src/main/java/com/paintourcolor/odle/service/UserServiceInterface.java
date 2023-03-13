package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.*;
import com.paintourcolor.odle.dto.user.response.*;
import com.paintourcolor.odle.entity.User;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserServiceInterface {
    void signupUser(UserSignupRequest userSignupRequest);
    void checkEmail(EmailCheckRequest emailCheckRequest);
    void checkUsername(UsernameCheckRequest usernameCheckRequest);
    void loginUser(UserLoginRequest userLoginRequest, HttpServletResponse response);
    void logoutUser(HttpServletRequest request);
    TokenReissueResponse reissueToken(HttpServletRequest request, HttpServletResponse response); //AccessToken 재발급
    void inactivateMe(User user, UserInactivateRequest userInactivateRequest);
    List<UserResponse> getUsers(Pageable pageable);
    List<ProfilePostResponse> getProfilePosts(Long userId, Pageable pageable);
    PostCountResponse getPostCount(Long userId);
    UserFollowOrUnfollowResponse getUserFollowOrUnfollowResponse(Long followingId, Long followerId);
}
package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.*;
import com.paintourcolor.odle.dto.user.response.PostCountResponse;
import com.paintourcolor.odle.dto.user.response.ProfilePostResponse;
import com.paintourcolor.odle.dto.user.response.UserFollowOrUnfollowResponse;
import com.paintourcolor.odle.dto.user.response.UserResponse;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.security.UserDetailsImpl;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserServiceInterface {
    void signupUser(UserSignupRequest userSignupRequest);
    void checkEmail(EmailCheckRequest emailCheckRequest);
    void checkUsername(UsernameCheckRequest usernameCheckRequest);
    void loginUser(UserLoginRequest userLoginRequest, HttpServletResponse response);
    void logoutUser(String token);  // ?
    void reissueToken(String refreshToken, HttpServletResponse response); //AccessToken 재발급
    void inactivateMe(User user, UserInactivateRequest userInactivateRequest); // 이거 리퀘스트 같이 써도 되는지,,,
    List<UserResponse> getUsers(Pageable pageable);
    List<ProfilePostResponse> getProfilePosts(Long userId, Pageable pageable);
    PostCountResponse getPostCount(Long userId);

    UserFollowOrUnfollowResponse getUserFollowOrUnfollowResponse(Long followingId, Long followerId);
}
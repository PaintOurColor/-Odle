package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.response.UserResponse;

public interface UserServiceInterface {
    void signupUser(UserSignupRequest userSignupRequest);
    void loginUser(UserLoginRequest userLoginRequest);
    void logoutUser(String username);  // ?
    void inactivateUser(String username, UserInactivateRequest userInactivateRequest); // 이거 리퀘스트 같이 써도 되는지,,,
    UserResponse getUser(int page);
}

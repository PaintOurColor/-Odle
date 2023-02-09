package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.dto.user.request.UserSignupRequest;
import com.paintourcolor.odle.dto.user.response.UserResponse;

public class UserService implements UserServiceInterface {
    // 유저 회원가입
    @Override
    public void signupUser(UserSignupRequest userSignupRequest) {

    }

    // 로그인 유저,관리자
    @Override
    public void loginUser(UserLoginRequest userLoginRequest) {

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

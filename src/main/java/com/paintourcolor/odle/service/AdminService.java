package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserActivateRequest;
import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminServiceInterface{
    // 관리자 회원가입
    @Override
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {

    }

    // 유저 활성화(관리자가)
    @Override
    public void activateUser(UserActivateRequest userActivateRequest, String username) {

    }

    // 유저 비활성화(관리자가)
    @Override
    public void inactivateUser(UserInactivateRequest userInactivateRequest, String username) {

    }
}
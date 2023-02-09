package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserActivateRequest;
import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;

public interface AdminServiceInterface {
    // 관리자 회원가입
    void signupAdmin(AdminSignupRequest adminSignupRequest);
    // 유저 활성화(관리자가)
    void activateUser(UserActivateRequest userActivateRequest, String username);
    // 유저 비활성화(관리자가)
    void inactivateUser(UserInactivateRequest userInactivateRequest, String username);
}

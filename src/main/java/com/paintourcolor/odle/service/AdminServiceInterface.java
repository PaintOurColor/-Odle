package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserActivateRequest;
import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.entity.UserRoleEnum;

public interface AdminServiceInterface {
    // 관리자 회원가입
    void signupAdmin(AdminSignupRequest adminSignupRequest);
    // 유저 활성화(관리자가)
    void activateUser(Long userId, User Admin, String adminPassword);
    // 유저 비활성화(관리자가)
    void inactivateUser(Long userId, User Admin, String adminPassword);
    // 게시글 삭제(관리자)
    void deletePost(Long postId, User Admin);
    // 댓글 삭제(관리자)
    void deleteComment(Long postId, Long commentId, User Admin);
}

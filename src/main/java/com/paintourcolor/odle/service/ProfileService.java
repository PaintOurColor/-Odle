package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.ProfileUpdateRequest;
import com.paintourcolor.odle.dto.user.response.ProfileResponse;
import com.paintourcolor.odle.dto.user.response.ProfileSimpleResponse;

public class ProfileService implements ProfileServiceInterface{
    // 프로필 설정(수정)
    @Override
    public void updateProfile(String username, ProfileUpdateRequest profileUpdateRequest) {

    }

    // 프로필 조회
    @Override
    public ProfileResponse getProfile(Long userId, String username) {
        return null;
    }

    // 간편 프로필 조회
    @Override
    public ProfileSimpleResponse getSimpleProfile(Long userId) {
        return null;
    }
}

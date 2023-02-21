package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.ProfileUpdateRequest;
import com.paintourcolor.odle.dto.user.response.ProfileResponse;
import com.paintourcolor.odle.dto.user.response.ProfileSimpleResponse;
import com.paintourcolor.odle.entity.User;

public interface ProfileServiceInterface {
    void updateProfile(Long userId, ProfileUpdateRequest profileUpdateRequest);
    ProfileResponse getProfile(Long userId);
    ProfileSimpleResponse getSimpleProfile(Long userId);
    ProfileSimpleResponse getMySimpleProfile(User user);
}

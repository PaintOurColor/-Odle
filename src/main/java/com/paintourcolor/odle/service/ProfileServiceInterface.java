package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.ProfileUpdateRequest;
import com.paintourcolor.odle.dto.user.response.ProfileResponse;
import com.paintourcolor.odle.dto.user.response.ProfileSimpleResponse;

public interface ProfileServiceInterface {
    void updateProfile(String username, ProfileUpdateRequest profileUpdateRequest);
    ProfileResponse getProfile(Long userId, String username);
    ProfileSimpleResponse getSimpleProfile(Long userId);

}

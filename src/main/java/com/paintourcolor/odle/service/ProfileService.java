package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.ProfileUpdateRequest;
import com.paintourcolor.odle.dto.user.response.FollowingResponse;
import com.paintourcolor.odle.dto.user.response.ProfileResponse;
import com.paintourcolor.odle.dto.user.response.ProfileSimpleResponse;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService implements ProfileServiceInterface{
    private final UserRepository userRepository;
    private final S3UploaderService s3UploaderService;


    // 프로필 설정(수정)
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequest profileUpdateRequest) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if (!profileUpdateRequest.getUsername().isEmpty())
            user.setUsername(profileUpdateRequest.getUsername());
        if(!profileUpdateRequest.getIntroduction().isEmpty())
             user.setIntroduction(profileUpdateRequest.getIntroduction());

        MultipartFile profileImage = profileUpdateRequest.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            String profileImageUrl = s3UploaderService.upload(profileImage, "profile");
            user.setProfileImage(profileImageUrl);
        }

        userRepository.save(user);
    }

    // 프로필 조회
    @Override
    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()->new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.")
        );
        return new ProfileResponse(user);
    }

    // 간편 프로필 조회
    @Override
    public ProfileSimpleResponse getSimpleProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()->new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.")
        );
        return new ProfileSimpleResponse(user);
    }

    @Override
    public ProfileSimpleResponse getMySimpleProfile(User user) {
    return new ProfileSimpleResponse(user);
    }
}

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

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService implements ProfileServiceInterface{
    private final UserRepository userRepository;

    // 프로필 설정(수정)
    @Override
    public void updateProfile(Long userId, ProfileUpdateRequest profileUpdateRequest) {
        String profileImage = profileUpdateRequest.getProfileImage();
        String introduction = profileUpdateRequest.getIntroduction();
        String username = profileUpdateRequest.getUsername();

        User user = userRepository.findById(userId).get();

        if(!username.equals(user.getUsername())){ //변경하려는 이름이 기존 이름과 다를 때만 중복확인
            if(userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("동일한 이름이 이미 존재합니다.");
            }
        }
        user.updateProfile(profileImage, introduction, username);
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

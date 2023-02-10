package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.AdminSignupRequest;
import com.paintourcolor.odle.dto.user.request.UserActivateRequest;
import com.paintourcolor.odle.dto.user.request.UserInactivateRequest;
import com.paintourcolor.odle.entity.ActivationEnum;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.entity.UserRoleEnum;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminServiceInterface{

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 관리자 회원가입
    @Override
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String username = adminSignupRequest.getUsername();
        String password = passwordEncoder.encode(adminSignupRequest.getPassword());
        String email = adminSignupRequest.getEmail();

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        });

        if (!adminSignupRequest.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
        }

        UserRoleEnum role = UserRoleEnum.ADMIN;
        ActivationEnum activation = ActivationEnum.ACTIVE;

        userRepository.save(new User(username, password, email, role, activation));
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
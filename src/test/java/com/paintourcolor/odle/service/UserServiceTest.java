package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.UserLoginRequest;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.entity.UserRoleEnum;
import com.paintourcolor.odle.repository.UserRepository;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void loginUser() {
    }

    @Test
    void signupUser() {
    }

    @Test
    void logoutUser() {
    }

    @Test
    void inactivateUser() {
    }

    @Test
    void getUser() {
    }
}

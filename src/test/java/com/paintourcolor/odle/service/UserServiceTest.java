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
    @DisplayName("로그인")
    void loginUser() {
    // given
        // 리퀘스트로 받아온 유저 로그인 정보
        UserLoginRequest request = UserLoginRequest.builder()
                .email("user1@gmail.com")
                .password("qweQWE123")
                .build();
        // 토큰 정보
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        // 저장되어있는 임의의 유저 객체
        User user1 = new User("user1@gmail.com", passwordEncoder.encode("qweQWE123"), UserRoleEnum.USER);
        // 저장되어있는 임의의 관리자 객체(그냥 일단 추가해봄)
        User admin1 = new User("admin1@gmail.com", passwordEncoder.encode("qweQWE123"), UserRoleEnum.ADMIN);

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(user1));

    // when
        userService.loginUser(request, servletResponse);

    // then
        verify(userRepository).saveAndFlush(any(User.class));

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

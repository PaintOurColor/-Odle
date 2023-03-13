package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String profileImage;
    private String introduction;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActivationEnum activation;

    public User(String email, String password, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, String email, UserRoleEnum role, ActivationEnum activation) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.activation = activation;
    }

    public void isAdmin() {
        if(!this.role.equals(UserRoleEnum.ADMIN)) {
            throw new AccessDeniedException("관리자만 이용 가능합니다.");
        }
    }

    public void isActivation( ) {
        if(!this.activation.equals(ActivationEnum.ACTIVE)) {
            throw new DisabledException("비활성화 된 계정입니다.");
        }
    }

    public void isInactivation( ) {
        if(!this.activation.equals(ActivationEnum.INACTIVE)) {
            throw new IllegalArgumentException("활성화 된 계정입니다.");
        }
    }

    public void matchPassword(String requestPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(requestPassword, this.password)){
            throw new BadCredentialsException("계정 혹은 비밀번호를 재확인 바랍니다.");
        }
    }

    public void updateProfile(String profileImage, String introduction, String username){
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.username = username;
    }

    public void updateActivation(ActivationEnum activation){
        this.activation = activation;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
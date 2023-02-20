package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String profileImage;
    private String introduction;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActivationEnum activation;

//    @ManyToMany
//    @JoinTable(
//            name = "LIKE_USER_POST",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id")
//    )
//    private List<Post> likePost = new ArrayList<>();
//    @Column(nullable = false)
//    private Long likeCount = 0L;

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

    public void isActivation( ) {
        if(!this.activation.equals(ActivationEnum.ACTIVE)) {
            throw new DisabledException("비활성화 된 계정입니다.");
        }
    }

    public void isInactivation( ) {
        if(!this.activation.equals(ActivationEnum.INACTIVE)) {
            throw new DisabledException("활성화 된 계정입니다.");
        }
    }

    public void matchPassword(String requestPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(requestPassword, this.password)){
            throw new BadCredentialsException("아이디 혹은 비밀번호가 일치하지 않습니다.");
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

}
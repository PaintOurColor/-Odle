package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String profileImage;
    private String introduction;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActivationEnum activation;

//    @Enumerated(EnumType.STRING)
//    private Authority authority;

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

    public boolean isActivation() {
        return this.activation.equals(ActivationEnum.ACTIVE);
    }
}
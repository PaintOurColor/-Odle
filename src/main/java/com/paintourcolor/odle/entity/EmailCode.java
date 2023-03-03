package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class EmailCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String email;
    @Column
    private String code;
    @Column
    @Enumerated(EnumType.STRING)
    private EmailVerifyEnum emailAuthentication;

    public EmailCode(String email, String code, EmailVerifyEnum emailAuthentication) {
        this.email = email;
        this.code = code;
        this.emailAuthentication = emailAuthentication;
    }

    public void updateCode(String code) {
        this.code = code;
    }

    public void updateVerify(EmailVerifyEnum verifyEnum) {
        this.emailAuthentication = verifyEnum;
    }
}

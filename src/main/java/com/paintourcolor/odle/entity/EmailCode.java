package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @CreationTimestamp // 생성되는 시간
    private LocalDateTime createdTime;

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

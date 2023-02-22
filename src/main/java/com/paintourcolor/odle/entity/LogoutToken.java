package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="BLACKLIST")
public class LogoutToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    String token;

    public LogoutToken(String token){
        this.token = token;
    }
}
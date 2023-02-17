package com.paintourcolor.odle.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "refresh_token")
    private String refreshToken;
}

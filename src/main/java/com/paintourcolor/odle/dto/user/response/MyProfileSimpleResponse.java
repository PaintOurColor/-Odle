package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class MyProfileSimpleResponse {
    Long userId;
    String username;
    String profileImage;
    Date tokenExpiration;

    public MyProfileSimpleResponse(User user, Date expiration) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.profileImage = user.getProfileImage();
        this.tokenExpiration = expiration;
    }
}

package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {
    Long userId;
    String email;
    String username;
    String profileImage;
    String introduction;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profileImage = user.getProfileImage();
        this.introduction = user.getIntroduction();
    }
}

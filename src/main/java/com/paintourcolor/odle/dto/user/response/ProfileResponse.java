package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponse {
    String email;
    String username;
    String profileImage;
    String introduction;

    public ProfileResponse(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profileImage = user.getProfileImage();
        this.introduction = user.getIntroduction();
    }
}

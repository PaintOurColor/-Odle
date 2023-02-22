package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileSimpleResponse {
    Long userId;
    String username;
    String profileImage;

    public ProfileSimpleResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.profileImage = user.getProfileImage();
    }
}

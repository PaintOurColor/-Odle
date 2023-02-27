package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.ActivationEnum;
import com.paintourcolor.odle.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {
    Long userId;
    String email;
    String username;
    ActivationEnum activation;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.activation = user.getActivation();
    }
}

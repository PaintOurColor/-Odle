package com.paintourcolor.odle.dto.user.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowerCountResponse {
    Long followerCount;

    public FollowerCountResponse(Long followerCount) {
        this.followerCount = followerCount;
    }
}

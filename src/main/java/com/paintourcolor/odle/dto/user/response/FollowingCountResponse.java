package com.paintourcolor.odle.dto.user.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowingCountResponse {
    Long followingCount;

    public FollowingCountResponse(Long followingCount) {
        this.followingCount = followingCount;
    }
}
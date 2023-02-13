package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowingResponse {
    Long followingId;
    String followingName;
    String followingProfileImage;

    public FollowingResponse(Follow follow) {
        this.followingId = follow.getFollowing().getId();
        this.followingName = follow.getFollowing().getUsername();
        this.followingProfileImage = follow.getFollowing().getProfileImage();
    }
}

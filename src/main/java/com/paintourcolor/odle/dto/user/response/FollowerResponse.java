package com.paintourcolor.odle.dto.user.response;

import com.paintourcolor.odle.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowerResponse {

    Long followerId;
    String followerName;
    String followerProfileImage;

    public FollowerResponse(Follow follow) {
        this.followerId = follow.getFollower().getId();
        this.followerName = follow.getFollower().getUsername();
        this.followerProfileImage = follow.getFollower().getProfileImage();
    }

}
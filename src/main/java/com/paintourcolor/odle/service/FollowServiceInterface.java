package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.response.FollowerCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowerResponse;
import com.paintourcolor.odle.dto.user.response.FollowingCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowingResponse;

public interface FollowServiceInterface {
    void followUser(Long userId, String username);
    void unfollowUser(Long userId, String username);
    FollowingResponse getFollowing(Long userId, int page);
    FollowerResponse getFollower(Long userId, int page);
    FollowingCountResponse countFollowing(Long userId);
    FollowerCountResponse countFollower(Long userId);
}

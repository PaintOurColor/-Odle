package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.response.FollowerCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowerResponse;
import com.paintourcolor.odle.dto.user.response.FollowingCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowingResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowServiceInterface {
    void followUser(Long followerId, Long followingId);
    void unfollowUser(Long followerId, Long followingId);
    List<FollowingResponse> getFollowings(Long userId, Pageable pageable);
    List<FollowerResponse> getFollowers(Long userId, Pageable pageable);
    FollowingCountResponse countFollowing(Long userId);
    FollowerCountResponse countFollower(Long userId);
}

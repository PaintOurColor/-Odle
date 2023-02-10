package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.response.FollowerCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowerResponse;
import com.paintourcolor.odle.dto.user.response.FollowingCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowingResponse;

public class FollowService implements FollowServiceInterface{
    // 팔로우 하기
    @Override
    public void followUser(Long userId, String username) {

    }

    // 팔로우 취소
    @Override
    public void unfollowUser(Long userId, String username) {

    }

    // 팔로우 조회
    @Override
    public FollowingResponse getFollowing(Long userId, int page) {
        return null;
    }

    // 팔로워 조회
    @Override
    public FollowerResponse getFollower(Long userId, int page) {
        return null;
    }

    // 팔로우 개수 조회
    @Override
    public FollowingCountResponse countFollowing(Long userId) {
        return null;
    }

    // 팔로워 개수 조회
    @Override
    public FollowerCountResponse countFollower(Long userId) {
        return null;
    }
}

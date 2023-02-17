package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.response.FollowerCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowerResponse;
import com.paintourcolor.odle.dto.user.response.FollowingCountResponse;
import com.paintourcolor.odle.dto.user.response.FollowingResponse;
import com.paintourcolor.odle.entity.Follow;
import com.paintourcolor.odle.entity.User;
import com.paintourcolor.odle.repository.FollowRepository;
import com.paintourcolor.odle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowServiceInterface{
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우 하기
    @Transactional
    @Override
    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId).get(); //자신
        User following = userRepository.findById(followingId).orElseThrow( //팔로우 당하는 사람
                ()-> new UsernameNotFoundException("팔로우할 유저를 찾을 수 없습니다.")
        );

        following.isActivation();

        Optional<Follow> followCheck = followRepository.findByFollowerIdAndFollowingId(followerId,followingId);
        if (followCheck.isPresent()) {
            throw new IllegalArgumentException("이미 팔로우한 대상입니다.");
        }

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }

    // 팔로우 취소
    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId,followingId).orElseThrow(
                ()->new IllegalArgumentException("팔로우 상태가 아닙니다.")
        );
        followRepository.deleteById(follow.getId());
    }

    // 팔로우 조회
    @Override
    public List<FollowingResponse> getFollowings(Long userId, Pageable pageable) {
        List<Follow> follows = followRepository.findAllByFollowerId(userId, pageable);

        return follows
                .stream()
                .map(FollowingResponse::new)
                .collect(Collectors.toList());
    }

    // 팔로워 조회
    @Override
    public List<FollowerResponse> getFollowers(Long userId, Pageable pageable) {
        List<Follow> follows = followRepository.findAllByFollowingId(userId, pageable);

        return follows
                .stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());
    }

    // 팔로워 개수 조회
    @Override
    public FollowerCountResponse countFollower(Long userId) {
        return new FollowerCountResponse(followRepository.countByFollowingId(userId));
    }


    // 팔로우 개수 조회
    @Override
    public FollowingCountResponse countFollowing(Long userId) {
        return new FollowingCountResponse(followRepository.countByFollowerId(userId));
    }
}

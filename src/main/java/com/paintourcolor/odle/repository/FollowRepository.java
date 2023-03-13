package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<Follow> findAllByFollowerId(Long followerId, Pageable pageable); //내가 팔로잉하는 사람들 목록
    List<Follow> findAllByFollowingId(Long followingId, Pageable pageable); //나를 팔로잉하는 사람들 목록
    Long countByFollowerId(Long followerId); //팔로잉 명수
    Long countByFollowingId(Long followingId); //팔로워 명수
    boolean existsFollowByFollowerIdAndFollowingId(Long followerId, Long followingId);
}

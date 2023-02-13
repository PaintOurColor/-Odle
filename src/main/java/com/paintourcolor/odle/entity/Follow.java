package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "followerId", nullable = false)
    private User follower; //팔로우 하는 사람
    @ManyToOne
    @JoinColumn(name = "followingId", nullable = false)
    private User following; //팔로우 당하는 사람

    public Follow (User follower, User following){
        this.follower= follower;
        this.following=following;
    }
}
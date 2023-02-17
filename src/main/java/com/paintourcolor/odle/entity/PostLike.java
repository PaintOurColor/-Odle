package com.paintourcolor.odle.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLike(User user, Post post ) {
        this.user = user;
        this.post = post;
    }

    public PostLike(User user, Post post, Long id) {
        this.user = user;
        this.post = post;
        this.id = id;
    }

}
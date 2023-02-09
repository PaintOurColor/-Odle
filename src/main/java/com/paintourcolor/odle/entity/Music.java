package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String singer;
    @Column(nullable = false)
    private String cover;
    @Column(nullable = false)
    private Long angryCount;
    @Column(nullable = false)
    private Long sadCount;
    @Column(nullable = false)
    private Long screamCount;
    @Column(nullable = false)
    private Long shyCount;
    @Column(nullable = false)
    private Long happyCount;
    @Column(nullable = false)
    private Long loveCount;
    @Column(nullable = false)
    private Long flexCount;
}
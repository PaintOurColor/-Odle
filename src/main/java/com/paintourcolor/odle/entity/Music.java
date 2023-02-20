package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "melonKoreaId", nullable = false)
    private Long melonKoreaId;
    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new LinkedHashSet<>();
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

    public Music(Long melonKoreaId, String title, String singer, String cover) {
        this.melonKoreaId = melonKoreaId;
        this.title = title;
        this.singer = singer;
        this.cover = cover;
        this.angryCount = 0L;
        this.sadCount = 0L;
        this.screamCount = 0L;
        this.shyCount = 0L;
        this.happyCount = 0L;
        this.loveCount = 0L;
        this.flexCount = 0L;
    }
    public void plusEmotionCount(EmotionEnum emotion) {
        switch (emotion) {
            case SAD -> sadCount++;
            case SHY -> shyCount++;
            case FLEX -> flexCount++;
            case LOVE -> loveCount++;
            case ANGRY -> angryCount++;
            case HAPPY -> happyCount++;
            case SCREAM -> screamCount++;
        }
    }

    public void minusEmotionCount(EmotionEnum emotion) {
        switch (emotion) {
            case SAD -> sadCount--;
            case SHY -> shyCount--;
            case FLEX -> flexCount--;
            case LOVE -> loveCount--;
            case ANGRY -> angryCount--;
            case HAPPY -> happyCount--;
            case SCREAM -> screamCount--;
        }
    }
}
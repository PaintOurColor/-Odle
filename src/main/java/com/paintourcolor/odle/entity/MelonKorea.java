package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class MelonKorea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String singer;
    @Column(nullable = false)
    private String cover;
    @OneToMany(mappedBy = "melonKorea", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Music> music = new LinkedHashSet<>();
}

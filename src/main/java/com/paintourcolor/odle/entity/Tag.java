package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tagName", nullable = false)
    private String tagName;

    @JoinColumn(name = "tagId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new LinkedHashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
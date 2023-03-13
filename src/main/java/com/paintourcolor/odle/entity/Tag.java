package com.paintourcolor.odle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // #로 시작  // 숫자, 영어 대소문자, 한글 가능  // 특문, 띄어쓰기 불가능
    @Column(name = "tagName", nullable = false)
    @Pattern(regexp = "^#([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+$")
    private String tagName;

    @Column(nullable = false)
    private Long tagCount;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
        this.tagCount = 1L;
    }

    public void plusTagCount() {
        this.tagCount += 1;
    }

    public void minusTagCount() {
        this.tagCount -= 1;
    }
}
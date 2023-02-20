package com.paintourcolor.odle.dto.mucis.response;

import lombok.Getter;

@Getter
public class MusicResponse {
    private final Long melonId;
    private final String title;
    private final String singer;
    private final String cover;

    public MusicResponse(Long melonId, String title, String singer, String cover) {
        this.melonId = melonId;
        this.title = title;
        this.singer = singer;
        this.cover = cover;
    }
}

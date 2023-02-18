package com.paintourcolor.odle.dto.mucis.response;

import lombok.Getter;

@Getter
public class MusicResponse {
    private String title;
    private String singer;
    private String cover;

    public MusicResponse(String title, String singer, String cover) {
        this.title = title;
        this.singer = singer;
        this.cover = cover;
    }
}

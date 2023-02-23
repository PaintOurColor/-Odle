package com.paintourcolor.odle.dto.mucis.response;

import com.paintourcolor.odle.entity.MelonKorea;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MusicSearchResponse {
    Long melonMusicId;
    String title;
    String singer;
    String cover;

    public MusicSearchResponse(MelonKorea melonKorea) {
        this.melonMusicId = melonKorea.getId();
        this.title = melonKorea.getTitle();
        this.singer = melonKorea.getSinger();
        this.cover = melonKorea.getCover();
    }
}

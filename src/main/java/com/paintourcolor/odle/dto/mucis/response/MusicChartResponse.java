package com.paintourcolor.odle.dto.mucis.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MusicChartResponse {
    private final Long musicId;
    private final String title;
    private final String singer;
    private final String cover;
    private final Long todayEmotionCount;

    public MusicChartResponse(Long musicId, String title, String singer, String cover, Long todayEmotionCount) {
        this.musicId = musicId;
        this.title = title;
        this.singer = singer;
        this.cover = cover;
        this.todayEmotionCount = todayEmotionCount;
    }
}

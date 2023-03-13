package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicChartResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicSearchResponse;
import com.paintourcolor.odle.entity.EmotionEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MusicServiceInterface {
    // 노래 정보 조회
    MusicResponse getMusic(Long musicId);
    List<MusicSearchResponse> getMusicSearchList(Pageable pageable, String searchOption, String keyword);
    List<MusicChartResponse> getEmotionChart(EmotionEnum emotion);
}

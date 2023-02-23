package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicSearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MusicServiceInterface {
    // 노래 정보 조회
    MusicResponse getMusic(Long musicId);
    List<MusicSearchResponse> getMusicSearchList(Pageable pageable, String searchOption, String keyword);
//    // 감정에 대한 TOP 포스팅 노래 리스트 조회(추가기능)
//    EmotionChartResponse getEmotionChart(EmotionChartRequest emotionChartRequest, int page);
//    // 오늘 가장 많이 포스팅된 TOP 노래 리스트 조회(추가기능)
//    TodayTopChartResponse getTodayTopChart(TodayTopChartRequest todayTopChartRequest, int page);
}

package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;

public interface MusicServiceInterface {
    // 노래 정보 조회
    MusicResponse getMusic(Long musicId);
//    // 감정에 대한 TOP 포스팅 노래 리스트 조회(추가기능)
//    EmotionChartResponse getEmotionChart(EmotionChartRequest emotionChartRequest, int page);
//    // 오늘 가장 많이 포스팅된 TOP 노래 리스트 조회(추가기능)
//    TodayTopChartResponse getTodayTopChart(TodayTopChartRequest todayTopChartRequest, int page);
}

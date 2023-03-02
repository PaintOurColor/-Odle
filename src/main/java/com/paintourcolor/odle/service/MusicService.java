package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicChartResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicSearchResponse;
import com.paintourcolor.odle.entity.MelonKorea;
import com.paintourcolor.odle.repository.MelonKoreaRepository;
import com.paintourcolor.odle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicService implements MusicServiceInterface {
    private final MelonKoreaRepository melonRepository;
    private final PostRepository postRepository;

    // 노래 정보 조회
    @Transactional
    @Override
    public MusicResponse getMusic(Long melonId) {
        MelonKorea melon = melonRepository.findById(melonId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 음악입니다.")
        );

        return new MusicResponse(melon.getId(), melon.getTitle(), melon.getSinger(), melon.getCover());
    }

    @Transactional
    @Override
    public List<MusicSearchResponse> getMusicSearchList(Pageable pageable, String searchOption, String keyword) {
            if(keyword.isEmpty()){
                throw new IllegalArgumentException("키워드 누락");
            }

            List<MelonKorea> musicSearchList = switch (searchOption) {
                case "제목" -> melonRepository.findAllByTitleContainsIgnoreCase(pageable, keyword);
                case "가수" -> melonRepository.findAllBySingerContainsIgnoreCase(pageable,keyword);
                default -> throw new IllegalArgumentException("옵션 누락");
            };

        if(musicSearchList.isEmpty()){
            throw new IllegalArgumentException("검색 결과가 없습니다");
        }

        return musicSearchList.stream()
                .map(MusicSearchResponse::new)
                .collect(Collectors.toList());
    }

    // angry 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getAngryChart() {
        // 24시간
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findAngryMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    // sad 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getSadChart() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findSadMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    // scream 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getScreamChart() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findScreamMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    // shy 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getShyChart() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findShyMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    // happy 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getHappyChart() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findHappyMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    // love 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getLoveChart() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findLoveMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    // flex 차트 조회
    @Transactional(readOnly = true)
    @Override
    public List<MusicChartResponse> getFlexChart() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<Object[]> musicList = postRepository.findFlexMusicIdsWithCountAndMusicInfo(startDate, endDate).stream().limit(6).toList();

        return musicChartResponses(musicList);
    }

    public List<MusicChartResponse> musicChartResponses(List<Object[]> musicList) {
        List<MusicChartResponse> musicChartResponses = new ArrayList<>();
        for (Object[] music : musicList) {
            MusicChartResponse musicChartResponse = MusicChartResponse.builder()
                    .musicId((Long) music[0])
                    .title((String) music[1])
                    .singer((String) music[2])
                    .cover((String) music[3])
                    .todayEmotionCount((Long) music[4])
                    .build();
            musicChartResponses.add(musicChartResponse);
        }
        return musicChartResponses;
    }
}

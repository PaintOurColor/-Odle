package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.mucis.response.MusicChartResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicSearchResponse;
import com.paintourcolor.odle.service.MusicServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/music")
public class MusicController {
    private final MusicServiceInterface musicService;

    // 노래 정보 조회
    @GetMapping("/{melonId}")
    public ResponseEntity<MusicResponse> getMusic(@PathVariable Long melonId) {
        MusicResponse musicResponse = musicService.getMusic(melonId);
        return new ResponseEntity<>(musicResponse, HttpStatus.OK);
    }

    //Param으로 option 및 keyword 가져옴
    //option: 제목, 가수
    //keyword: 입력한 키워드를 포함하는 모든 음악 리스트를 가져옴
    //페이지 기본 설정: 음악 수-10, 첫페이지-1(index:0)
    //(정렬을 최신순으로 하고 싶었으나, 크롤링 시 한번에 가져온 데이터는 정렬이 불가할 것 같음)
    @GetMapping("/search/{option}/{keyword}")
    public ResponseEntity<List<MusicSearchResponse>> getMusicSearchList(Pageable pageable,
                                                                        @PathVariable String option,
                                                                        @PathVariable String keyword) {
        List<MusicSearchResponse> musicSearchList = musicService.getMusicSearchList(pageable, option, keyword);
        return new ResponseEntity<>(musicSearchList, HttpStatus.OK);
    }

    // angry 차트 조회
    @GetMapping("/charts/angry")
    public ResponseEntity<List<MusicChartResponse>> getAngryChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getAngryChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }

    // sad 차트 조회
    @GetMapping("/charts/sad")
    public ResponseEntity<List<MusicChartResponse>> getSadChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getSadChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }

    // scream 차트 조회
    @GetMapping("/charts/scream")
    public ResponseEntity<List<MusicChartResponse>> getScreamChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getScreamChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }

    // shy 차트 조회
    @GetMapping("/charts/shy")
    public ResponseEntity<List<MusicChartResponse>> getShyChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getShyChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }

    // happy 차트 조회
    @GetMapping("/charts/happy")
    public ResponseEntity<List<MusicChartResponse>> getHappyChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getHappyChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }

    // love 차트 조회
    @GetMapping("/charts/love")
    public ResponseEntity<List<MusicChartResponse>> getLoveChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getLoveChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }

    // flex 차트 조회
    @GetMapping("/charts/flex")
    public ResponseEntity<List<MusicChartResponse>> getFlexChart() {
        List<MusicChartResponse> musicChartResponses = musicService.getFlexChart();
        return new ResponseEntity<>(musicChartResponses, HttpStatus.OK);
    }
}

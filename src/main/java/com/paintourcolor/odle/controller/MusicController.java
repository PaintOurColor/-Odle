package com.paintourcolor.odle.controller;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.service.MusicServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

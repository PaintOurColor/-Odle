package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.entity.Music;
import com.paintourcolor.odle.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService implements MusicServiceInterface {
    private final MusicRepository musicRepository;
    // 노래 정보 조회
    @Override
    public MusicResponse getMusic(Long musicId) {
        Music music = musicRepository.findById(musicId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 음악입니다.")
        );

        return new MusicResponse(music.getTitle(), music.getSinger(), music.getCover());
    }
}

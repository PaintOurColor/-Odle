package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.entity.MelonKorea;
import com.paintourcolor.odle.repository.MelonKoreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService implements MusicServiceInterface {
    private final MelonKoreaRepository melonRepository;
    // 노래 정보 조회
    @Override
    public MusicResponse getMusic(Long melonId) {
        MelonKorea melon = melonRepository.findById(melonId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 음악입니다.")
        );

        return new MusicResponse(melon.getId(), melon.getTitle(), melon.getSinger(), melon.getCover());
    }
}

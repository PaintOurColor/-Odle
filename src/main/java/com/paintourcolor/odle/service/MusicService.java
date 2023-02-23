package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.mucis.response.MusicResponse;
import com.paintourcolor.odle.dto.mucis.response.MusicSearchResponse;
import com.paintourcolor.odle.entity.MelonKorea;
import com.paintourcolor.odle.repository.MelonKoreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicService implements MusicServiceInterface {
    private final MelonKoreaRepository melonRepository;
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

}

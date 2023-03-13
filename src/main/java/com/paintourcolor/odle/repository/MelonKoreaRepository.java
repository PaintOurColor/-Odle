package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.MelonKorea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MelonKoreaRepository extends JpaRepository<MelonKorea, Long> {
    List<MelonKorea> findAllByTitleContainsIgnoreCase(Pageable pageable, String keyword);
    List<MelonKorea> findAllBySingerContainsIgnoreCase(Pageable pageable, String keyword);
}

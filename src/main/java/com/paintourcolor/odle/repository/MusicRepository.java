package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}

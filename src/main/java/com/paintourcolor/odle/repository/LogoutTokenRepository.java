package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoutTokenRepository extends JpaRepository<LogoutToken, Long> {
    boolean existsByToken(String token);
}
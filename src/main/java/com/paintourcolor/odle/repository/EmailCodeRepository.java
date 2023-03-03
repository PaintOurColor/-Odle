package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
    boolean existsByEmail(String userEmail);
    EmailCode findByEmail(String userEmail);
}

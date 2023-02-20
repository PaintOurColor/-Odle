package com.paintourcolor.odle.repository;

import com.paintourcolor.odle.entity.ActivationEnum;
import com.paintourcolor.odle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
}
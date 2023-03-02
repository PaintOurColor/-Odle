package com.paintourcolor.odle.util.redis;

import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService implements RedisServiceInterface {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveToken(String refreshToken, LocalDateTime loginTime) {
        String loginTimeText = "Login Time: " + loginTime;
        redisTemplate.opsForValue().set(refreshToken, loginTimeText, JwtUtil.REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS);
    }

    @Override
    public void deleteToken(String refreshToken) {
        redisTemplate.delete(JwtUtil.REFRESH_PREFIX + refreshToken);
    }

    @Override
    public void existsToken(String refreshToken) {
        if (!redisTemplate.hasKey(JwtUtil.REFRESH_PREFIX + refreshToken)) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
    }
}

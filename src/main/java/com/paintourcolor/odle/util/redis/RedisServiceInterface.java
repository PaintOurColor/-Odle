package com.paintourcolor.odle.util.redis;

import java.time.LocalDateTime;

public interface RedisServiceInterface {
    void saveToken(String refreshToken, LocalDateTime loginTime);
    void deleteToken(String refreshToken);
    void existsToken(String refreshToken);
}

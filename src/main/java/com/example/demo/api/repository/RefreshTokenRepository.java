package com.example.demo.api.repository;

import com.example.demo.api.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
    private RedisTemplate redisTemplate;

    @Value("${spring.jwt.expire.refresh}")
    private int expiredRefreshToken;

    public RefreshTokenRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getEmail());
        redisTemplate.expire(refreshToken.getRefreshToken(), Duration.ofDays(expiredRefreshToken).toSeconds(), TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findById(String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String userId = valueOperations.get(refreshToken);

        if (Objects.isNull(userId)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, userId));
    }
}

package com.penny.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    @Value("${spring.data.redis.ttl-in-min}")
    private Integer ttl;

    private final RedisTemplate<String, String> redisTemplate;

    public void setStr(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl, TimeUnit.MINUTES);
    }

    public String getStr(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}

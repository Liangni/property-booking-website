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

    /**
     * 將字串存入 Redis，並設置過期時間。
     *
     * @param key   存入 Redis 的鍵
     * @param value 要存入的字串值
     */
    public void setStr(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl, TimeUnit.MINUTES);
    }

    /**
     * 從 Redis 中獲取指定鍵的字串值。
     *
     * @param key 要獲取值的鍵
     * @return Redis 中對應鍵的字串值，若不存在則返回 null
     */
    public String getStr(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 從 Redis 中刪除指定鍵的字串數據。
     *
     * @param key 要刪除的鍵
     */
    public void deleteStr(String key) {
        redisTemplate.delete(key);
    }
}

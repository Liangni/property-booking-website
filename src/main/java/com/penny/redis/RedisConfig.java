package com.penny.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 創建並配置用於操作 Redis 的 RedisTemplate 實例。
     *
     * @param connectionFactory RedisConnectionFactory 實例，用於建立 Redis 連接
     * @return 配置好的 RedisTemplate 實例，用於操作 Redis 中的字串數據
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 創建 RedisTemplate 實例
        RedisTemplate<String, String> template = new RedisTemplate<>();

        // 設置 Redis 連接工廠
        template.setConnectionFactory(connectionFactory);

        // 設置默認的序列化器，這裡使用 Jackson2JsonRedisSerializer 來序列化任意類型的物件
        template.setDefaultSerializer(
                new Jackson2JsonRedisSerializer<>(Object.class));

        // 啟用事務支持
        template.setEnableTransactionSupport(true);

        // 確保 Bean 設置完成後再進行初始
        template.afterPropertiesSet();

        return template;
    }
}

package com.example.library.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisStartupCheckConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ApplicationRunner redisStartupCheck() {
        return args -> {
            try (var connection = redisConnectionFactory.getConnection()) {
                String pong = connection.ping();
                if (!"PONG".equalsIgnoreCase(pong)) {
                    throw new IllegalStateException("Redis ping failed");
                }
                log.info("Redis is available");
            } catch (Exception ex) {
                log.error("Redis is NOT available", ex);
                throw new IllegalStateException("Redis is required, but not available", ex);
            }
        };
    }
}

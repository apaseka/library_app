package com.example.library.integration.redis;

import com.example.library.LibraryApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import java.net.ConnectException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RedisFailFastTest {

    @Test
    void shouldFailContextIfRedisUnavailable() {
        SpringApplication app = new SpringApplication(LibraryApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);

        assertThatThrownBy(() ->
                app.run(
                        "--spring.data.redis.host=localhost",
                        "--spring.data.redis.port=6390"
                )
        ).hasRootCauseInstanceOf(ConnectException.class)
                .hasMessageContaining("Redis is required");
    }
}

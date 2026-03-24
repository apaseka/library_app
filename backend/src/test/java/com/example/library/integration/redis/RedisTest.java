package com.example.library.integration.redis;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.entity.Author;
import com.example.library.repository.AuthorRepository;
import com.example.library.service.AuthorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.profiles.active=h2Test")
public class RedisTest {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @MockitoSpyBean
    private AuthorRepository authorRepository;

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7")
                    .withExposedPorts(6379)
                    .withReuse(true);

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port",
                () -> redis.getMappedPort(6379));
    }

    @BeforeAll
    static void startContainers() {
        redis.start();
    }

    @BeforeEach
    void clearAll() {
        redisConnectionFactory.getConnection().serverCommands().flushAll();
    }

    @Test
    void shouldUseCache() {
        authorService.getAll();
        authorService.getAll();

        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void shouldEvictCache() {
        CreateAuthorRequest request = new CreateAuthorRequest("George Orwell");

        authorService.getAll();
        authorService.save(request);
        authorService.getAll();

        verify(authorRepository, times(2)).findAll();
        verify(authorRepository, times(1)).save(any(Author.class));
    }
}

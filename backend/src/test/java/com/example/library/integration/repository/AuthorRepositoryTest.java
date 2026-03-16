package com.example.library.integration.repository;

import com.example.library.entity.Author;
import com.example.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=postgresTest")
@Testcontainers
class AuthorRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18.3")
                    .withDatabaseName("library")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    AuthorRepository authorRepository;

    @BeforeEach
    void setup() {
        authorRepository.deleteAll();
    }

    @Test
    void shouldFindAuthorByName() {

        Author author = Author.builder()
                .name("Robert Martin")
                .build();

        authorRepository.save(author);

        Optional<Author> result = authorRepository.findByName("Robert Martin");

        assertTrue(result.isPresent());
        assertEquals("Robert Martin", result.get().getName());
    }

    @Test
    void shouldReturnTrueIfAuthorExists() {

        Author author = Author.builder()
                .name("Robert Martin")
                .build();

        authorRepository.save(author);

        boolean exists = authorRepository.existsByName("Robert Martin");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseOnExistsByName() {
        boolean exists = authorRepository.existsByName("Unknown");
        assertFalse(exists);
    }

    @Test
    void shouldReturnEmptyIfAuthorNotFound() {
        Optional<Author> result = authorRepository.findByName("Unknown");
        assertTrue(result.isEmpty());
    }
}
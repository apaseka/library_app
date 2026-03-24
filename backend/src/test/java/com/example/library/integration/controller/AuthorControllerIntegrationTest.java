package com.example.library.integration.controller;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.entity.Author;
import com.example.library.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=h2test")
@AutoConfigureMockMvc
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        authorRepository.deleteAll();
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        CreateAuthorRequest request = new CreateAuthorRequest("Jane Austen");

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Jane Austen"))
                .andExpect(jsonPath("$.data.id").isNumber());
    }

    @Test
    void shouldNotCreateDuplicateAuthor() throws Exception {
        authorRepository.save(new Author(null, "Duplicate", null));

        CreateAuthorRequest request = new CreateAuthorRequest("Duplicate");

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Author \"Duplicate\" already exists"));
    }

    @Test
    void shouldReturnAllAuthors() throws Exception {
        authorRepository.saveAll(List.of(
                new Author(null, "Author 1", null),
                new Author(null, "Author 2", null)
        ));

        ResultActions perform = mockMvc.perform(get("/authors"));
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data.[?(@.name=='Author 1')]").exists())
                .andExpect(jsonPath("$.data.[?(@.name=='Author 2')]").exists());
    }

    @Test
    void shouldFailValidationForBlankName() throws Exception {
        CreateAuthorRequest request = new CreateAuthorRequest("");

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.name").value("Author name must not be blank"));
    }

    @Test
    void shouldFailValidationForTooLongName() throws Exception {
        String longName = "A".repeat(101);
        CreateAuthorRequest request = new CreateAuthorRequest(longName);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.name").value("Author name must be shorter than 100 characters"));
    }
}
package com.example.library.integration.controller;

import com.example.library.dto.request.CreateUpdateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=h2test")
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Author author1;
    private Author author2;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        author1 = authorRepository.save(new Author(null, "Robert Martin", null));
        author2 = authorRepository.save(new Author(null, "James Joyce", null));
    }

    @Test
    void shouldCreateBookWithExistingAuthor() throws Exception {
        CreateUpdateBookRequest request = new CreateUpdateBookRequest("Clean Code", 2008, author1.getName());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin - 13"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldNotCreateBookWithNonExistentAuthor() throws Exception {
        CreateUpdateBookRequest request = new CreateUpdateBookRequest("New Book", 2020, "Unknown Author");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Author \"Unknown Author\" not found"));
    }

    @Test
    void shouldReturnAndMapAllBooksToDtoEvenWithLazyAuthor() throws Exception {
        bookRepository.saveAll(List.of(
                new Book(null, "Clean Code", author1, 2008),
                new Book(null, "Ulysses", author2, 1920)
        ));

        mockMvc.perform(get("/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin - 13"))
                .andExpect(jsonPath("$[1].title").value("Ulysses"))
                .andExpect(jsonPath("$[1].author").value("James Joyce - 11"));
    }

    @Test
    void shouldGetBookById() throws Exception {
        Book book = bookRepository.save(new Book(null, "Clean Code", author1, 2008));

        mockMvc.perform(get("/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin - 13"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentBook() throws Exception {
        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with id 999 not found"));
    }

    @Test
    void shouldUpdateBookAndChangeAuthor() throws Exception {
        Book book = bookRepository.save(new Book(null, "Old Title", author1, 2000));

//        BookDTO dto = new BookDTO(book.getId(), "Clean Architecture", 2017, author2.getName());
        CreateUpdateBookRequest request = new CreateUpdateBookRequest("Clean Architecture", 2017, author2.getName());

        mockMvc.perform(put("/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Architecture"))
                .andExpect(jsonPath("$.year").value(2017))
                .andExpect(jsonPath("$.author").value("James Joyce - 11"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        Book book = bookRepository.save(new Book(null, "To Delete", author1, 2010));

        mockMvc.perform(delete("/books/{id}", book.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/{id}", book.getId()))
                .andExpect(status().isNotFound());
    }
}
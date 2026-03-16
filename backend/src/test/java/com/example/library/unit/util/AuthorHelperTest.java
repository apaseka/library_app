package com.example.library.unit.util;

import com.example.library.entity.Author;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.AuthorRepository;
import com.example.library.util.AuthorHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorHelperTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorHelper authorHelper;

    @Test
    void shouldReturnAuthorIfExists() {

        Author author = Author.builder()
                .id(1L)
                .name("Robert Martin")
                .build();

        when(authorRepository.findByName("Robert Martin"))
                .thenReturn(Optional.of(author));

        Author result = authorHelper.getAuthorIfExists("Robert Martin");

        assertEquals("Robert Martin", result.getName());
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFound() {

        when(authorRepository.findByName("Unknown"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                authorHelper.getAuthorIfExists("Unknown")
        );
    }

    @Test
    void shouldThrowExceptionIfAuthorAlreadyExists() {

        when(authorRepository.existsByName("Robert Martin"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                authorHelper.ensureAuthorDoesNotExist("Robert Martin")
        );
    }

    @Test
    void shouldNotThrowIfAuthorDoesNotExist() {

        when(authorRepository.existsByName("Robert Martin"))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                authorHelper.ensureAuthorDoesNotExist("Robert Martin")
        );
    }
}
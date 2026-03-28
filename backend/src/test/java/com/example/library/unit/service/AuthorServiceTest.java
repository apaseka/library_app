package com.example.library.unit.service;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.entity.Author;
import com.example.library.mapper.AuthorMapper;
import com.example.library.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorServiceTest extends ServiceTest {

    @InjectMocks
    private AuthorService authorService;
    @Mock
    private AuthorMapper authorMapper;

    @Test
    void shouldReturnAllAuthors() {

        List<Author> authors = List.of(
                createAuthor(1L, "First"),
                createAuthor(2L, "Second")
        );

        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toDto(any(Author.class)))
                .thenAnswer(inv -> {
                    Author a = inv.getArgument(0);
                    return new AuthorDTO(a.getId(), a.getName());
                });

        LibraryResponse<List<AuthorDTO>> response = authorService.getAll();
        List<AuthorDTO> result = response.getData();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("First", result.getFirst().getName());
        assertTrue(result.stream().anyMatch(a -> a.getName().equals("Second")));
        verify(authorRepository).findAll();
    }

    @Test
    void shouldSaveAuthor() {
        Author author = createAuthor(1L, "Jane Austen");
        CreateAuthorRequest request = new CreateAuthorRequest("Jane Austen");

        doNothing().when(authorHelper).ensureAuthorDoesNotExist(author.getName());
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.toDto(any(Author.class)))
                .thenAnswer(inv -> {
                    Author a = inv.getArgument(0);
                    return new AuthorDTO(a.getId(), a.getName());
                });
        when(authorMapper.toEntity(any(CreateAuthorRequest.class)))
                .thenAnswer(inv -> {
                    CreateAuthorRequest r = inv.getArgument(0);
                    return Author.builder().name(r.name()).build();
                });

        LibraryResponse<AuthorDTO> response = authorService.save(request);
        AuthorDTO result = response.getData();

        assertEquals(1L, result.getId());
        assertEquals("Jane Austen", result.getName());

        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {

        String existingAuthor = "Existing Author";
        CreateAuthorRequest request = new CreateAuthorRequest(existingAuthor);

        doThrow(new IllegalArgumentException())
                .when(authorHelper)
                .ensureAuthorDoesNotExist(existingAuthor);

        assertThrows(RuntimeException.class, () -> {
            authorService.save(request);
        });

        verify(authorRepository, never()).save(any(Author.class));
    }
}
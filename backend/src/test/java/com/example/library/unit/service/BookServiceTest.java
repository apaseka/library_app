package com.example.library.unit.service;

import com.example.library.dto.request.CreateUpdateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest extends ServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    private Book book;

    @BeforeEach
    void setup() {
        Author author = createAuthor(1L, "Jane Austen");
        book = createBook(1L, "Pride and Prejudice", 1813, author);
    }

    @Test
    void shouldReturnAllBooks() {

        Book firstBook = book;
        Book secondBook = createBook(1L, "Sense and Sensibility", 1811, book.getAuthor());

        List<Book> bookEntities = List.of(firstBook, secondBook);

        when(bookRepository.findAll()).thenReturn(bookEntities);
        when(bookMapper.toDto(any(Book.class)))
                .thenAnswer(inv -> {
                    Book b = inv.getArgument(0);
                    return new BookDTO(b.getId(), b.getTitle(), b.getYear(), b.getAuthor().getName());
                });

        LibraryResponse<List<BookDTO>> response = bookService.getAll();
        List<BookDTO> books = response.getData();

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(2, books.size());
        assertEquals("Pride and Prejudice", books.getFirst().getTitle());
        assertTrue(books.stream().anyMatch(b -> b.getAuthor().equals("Jane Austen")));
        verify(bookRepository).findAll();
    }

    @Test
    void shouldReturnBookById() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(any(Book.class)))
                .thenAnswer(inv -> {
                    Book b = inv.getArgument(0);
                    return new BookDTO(b.getId(), b.getTitle(), b.getYear(), b.getAuthor().getName());
                });

        LibraryResponse<BookDTO> response = bookService.getById(1L);
        BookDTO result = response.getData();

        assertEquals(1L, result.getId());
        assertEquals("Pride and Prejudice", result.getTitle());
        assertEquals("Jane Austen", result.getAuthor());
        verify(bookRepository).findById(1L);
    }

    @Test
    void shouldCreateBook() {
        CreateUpdateBookRequest request = new CreateUpdateBookRequest("Pride and Prejudice", 1813, "Jane Austen");

        when(authorHelper.getAuthorIfExists(request.author())).thenReturn(book.getAuthor());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toEntity(any(CreateUpdateBookRequest.class), any(Author.class)))
                .thenAnswer(inv -> {
                    CreateUpdateBookRequest r = inv.getArgument(0);
                    Author a = inv.getArgument(1);
                    return Book.builder()
                            .title(r.title())
                            .year(r.year())
                            .author(a)
                            .build();
                });
        when(bookMapper.toDto(any(Book.class)))
                .thenAnswer(inv -> {
                    Book b = inv.getArgument(0);
                    return new BookDTO(b.getId(), b.getTitle(), b.getYear(), b.getAuthor().getName());
                });

        LibraryResponse<BookDTO> response = bookService.create(request);
        BookDTO result = response.getData();

        assertEquals(1L, result.getId());
        assertEquals("Pride and Prejudice", result.getTitle());
        assertEquals("Jane Austen", result.getAuthor());
        verify(bookRepository).save(any(Book.class));
        verify(authorHelper).getAuthorIfExists(any());
    }

    @Test
    void shouldUpdateBook() {
        CreateUpdateBookRequest request = new CreateUpdateBookRequest("Pride and Prejudice new edition", 1813, "Jane Austen");

        when(authorHelper.getAuthorIfExists(request.author())).thenReturn(book.getAuthor());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(any(Book.class)))
                .thenAnswer(inv -> {
                    Book b = inv.getArgument(0);
                    return new BookDTO(b.getId(), b.getTitle(), b.getYear(), b.getAuthor().getName());
                });

        LibraryResponse<BookDTO> response = bookService.update(1L, request);
        BookDTO result = response.getData();

        assertEquals(1L, result.getId());
        assertEquals("Pride and Prejudice new edition", result.getTitle());
        assertEquals("Jane Austen", result.getAuthor());
        verify(bookRepository).findById(1L);
        verify(authorHelper).getAuthorIfExists(any());
    }

    @Test
    void shouldDeleteBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.deleteById(1L);
        verify(bookRepository).delete(book);
    }
}
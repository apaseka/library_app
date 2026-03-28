package com.example.library.service;

import com.example.library.dto.request.CreateUpdateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.BookRepository;
import com.example.library.util.AuthorHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorHelper authorHelper;

    @Cacheable(value = "books", key = "'all'")
    public LibraryResponse<List<BookDTO>> getAll() {
        log.debug("Fetching all books");
        List<BookDTO> books = bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .toList();
        log.debug("Fetched {} books", books.size());
        return new LibraryResponse<>(books);
    }

    @Cacheable(value = "books", key = "#id")
    public LibraryResponse<BookDTO> getById(Long id) {
        log.debug("Cache miss for books::id, loading book from database");
        log.info("Getting book by id={}", id);

        try {
            Book book = getBookByIdOrThrow(id);
            return new LibraryResponse<>(BookMapper.toDto(book));
        } catch (Exception ex) {
            log.error("Failed to get book id={}", id, ex);
            throw ex;
        }
    }

    @Transactional
    @CacheEvict(value = "books", key = "'all'")
    public LibraryResponse<BookDTO> create(CreateUpdateBookRequest request) {
        log.info("Creating book: title={}, author={} and evicting cache books::all", request.title(), request.author());

        Author author = authorHelper.getAuthorIfExists(request.author());

        Book book = BookMapper.toEntity(request, author);
        Book savedBook = bookRepository.save(book);

        log.info("Book created with id={}, title={}", savedBook.getId(), savedBook.getTitle());

        return new LibraryResponse<>(BookMapper.toDto(savedBook));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "'all'"),
            @CacheEvict(value = "books", key = "#result.data.id", condition = "#result != null")
    })
    public LibraryResponse<BookDTO> update(Long id, CreateUpdateBookRequest request) {
        log.info("Updating book: title={}, author={}, id={} and evicting cache books::all, books::id", request.title(), request.author(), id);
        Book book = getBookByIdOrThrow(id);
        Author author = authorHelper.getAuthorIfExists(request.author());

        book.setYear(request.year());
        book.setTitle(request.title());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        log.info("Book updated with id={}", savedBook.getId());
        BookDTO dto = BookMapper.toDto(savedBook);
        return new LibraryResponse<>(dto);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "'all'"),
            @CacheEvict(value = "books", key = "#id")
    })
    public void deleteById(Long id) {
        log.info("Deleting book id={}", id);
        Book book = getBookByIdOrThrow(id);
        log.info("Book deleted id={}", id);
        bookRepository.delete(book);
    }

    private @NonNull Book getBookByIdOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Book with id %d not found", id)
                ));
    }
}
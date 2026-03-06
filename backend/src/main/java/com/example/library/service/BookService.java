package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookDTO> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .toList();
    }

    public BookDTO getById(Long id) {
        Book book = getBookByIdOrThrow(id);
        return BookMapper.toDto(book);
    }

    private @NonNull Book getBookByIdOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Book with id %d not found", id)
                ));
    }

    public BookDTO create(BookDTO dto) {

        Author author = findAuthorOrThrow(dto.getAuthor());
        Book book = BookMapper.toEntity(dto, author);

        Book savedBook = bookRepository.save(book);

        return BookMapper.toDto(book);
    }

    @Transactional
    public BookDTO update(Long id, BookDTO dto) {
        Book book = getBookByIdOrThrow(id);
        Author author = findAuthorOrThrow(dto.getAuthor());

        book.setYear(dto.getYear());
        book.setTitle(dto.getTitle());
        book.setAuthor(author);

        /*
        No need to call save() here. The entity is managed by JPA inside the transaction,
        so Hibernate will automatically persist changes (dirty checking).
        Book savedBook = bookRepository.save(book);
         */

        return BookMapper.toDto(book);
    }

    private Author findAuthorOrThrow(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Author \"%s\" not found", authorName)
                ));
    }

    public void deleteById(Long id) {
        Book book = getBookByIdOrThrow(id);
        bookRepository.delete(book);
    }
}
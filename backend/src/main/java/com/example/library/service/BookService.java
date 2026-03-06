package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookDTO> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .toList();
    }

    public BookDTO create(BookDTO dto) {

        Author author = findAuthorOrThrow(dto.getAuthor());
        Book book = BookMapper.toEntity(dto, author);
        Book savedBook = bookRepository.save(book);

        return BookMapper.toDto(savedBook);
    }

    private Author findAuthorOrThrow(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Author \"%s\" not found", authorName)
                ));
    }
}
package com.example.library.service;

import com.example.library.dto.request.CreateUpdateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.BookRepository;
import com.example.library.util.AuthorHelper;
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
    private final AuthorHelper authorHelper;

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

    @Transactional
    public BookDTO create(CreateUpdateBookRequest request) {

        Author author = authorHelper.getAuthorIfExists(request.author());
        Book book = BookMapper.toEntity(request, author);
        Book savedBook = bookRepository.save(book);
        return BookMapper.toDto(savedBook);
    }

    @Transactional
    public BookDTO update(Long id, CreateUpdateBookRequest request) {
        Book book = getBookByIdOrThrow(id);
        Author author = authorHelper.getAuthorIfExists(request.author());

        book.setYear(request.year());
        book.setTitle(request.title());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return BookMapper.toDto(savedBook);
    }

    @Transactional
    public void deleteById(Long id) {
        Book book = getBookByIdOrThrow(id);
        bookRepository.delete(book);
    }

    private @NonNull Book getBookByIdOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Book with id %d not found", id)
                ));
    }
}
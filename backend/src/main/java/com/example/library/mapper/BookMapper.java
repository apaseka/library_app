package com.example.library.mapper;

import com.example.library.dto.request.CreateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;

public class BookMapper {

    public static BookDTO toDto(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getYear(),

// TODO: Accessing book.getAuthor().getName() outside an active transaction
// may cause LazyInitializationException due to lazy loading.
// Solution: either keep the transaction open or use fetch join / DTO projection.
                book.getAuthor().getName()
        );
    }

    public static Book toEntity(CreateBookRequest request, Author author) {
        return Book.builder()
                .title(request.title())
                .year(request.year())
                .author(author)
                .build();
    }
}
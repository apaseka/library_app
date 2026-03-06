package com.example.library.mapper;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;

public class BookMapper {

    public static BookDTO toDto(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthor().getName()
        );
    }

    public static Book toEntity(BookDTO dto, Author author) {
        return Book.builder()
                .title(dto.getTitle())
                .year(dto.getYear())
                .author(author)
                .build();
    }
}
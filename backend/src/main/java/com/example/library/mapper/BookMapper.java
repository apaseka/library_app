package com.example.library.mapper;

import com.example.library.dto.BookDTO;
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
}
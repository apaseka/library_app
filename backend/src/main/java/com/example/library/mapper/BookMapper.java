package com.example.library.mapper;

import com.example.library.dto.request.CreateUpdateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "author.name", target = "author")
    BookDTO toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "author")
    Book toEntity(CreateUpdateBookRequest request, Author author);
}
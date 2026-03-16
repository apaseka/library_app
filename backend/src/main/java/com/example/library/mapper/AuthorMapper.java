package com.example.library.mapper;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.entity.Author;

public class AuthorMapper extends CommonMapper {

    public static AuthorDTO toDto(Author author) {
        return new AuthorDTO(author.getId(), modifyStringForTests(author.getName()));
    }

    public static Author toEntity(CreateAuthorRequest request) {
        return Author.builder()
                .name(request.name())
                .build();
    }

}

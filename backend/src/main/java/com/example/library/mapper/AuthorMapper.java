package com.example.library.mapper;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.entity.Author;

public final class AuthorMapper {

    private AuthorMapper() {
    }

    public static AuthorDTO toDto(Author author) {
        return new AuthorDTO(author.getId(), author.getName());
    }

    public static Author toEntity(CreateAuthorRequest request) {
        return Author.builder()
                .name(request.name())
                .build();
    }

}

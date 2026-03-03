package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.entity.Author;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDTO> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AuthorDTO save(AuthorDTO dto) {
        Author author = Author.builder()
                .name(dto.getName())
                .build();

        Author saved = authorRepository.save(author);

        return toDto(saved);
    }

    private AuthorDTO toDto(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getName()
        );
    }
}
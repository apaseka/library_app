package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.entity.Author;
import com.example.library.mapper.AuthorMapper;
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
                .map(AuthorMapper::toDto)
                .toList();
    }

    public AuthorDTO save(AuthorDTO dto) {

        Author author = AuthorMapper.toEntity(dto);

        Author saved = authorRepository.save(author);

        return AuthorMapper.toDto(saved);
    }

}
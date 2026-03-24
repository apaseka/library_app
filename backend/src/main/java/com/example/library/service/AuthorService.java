package com.example.library.service;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.entity.Author;
import com.example.library.mapper.AuthorMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.util.AuthorHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorHelper authorHelper;

    @Cacheable(value = "authors", key = "'all'")
    public LibraryResponse<List<AuthorDTO>> getAll() {
        List<AuthorDTO> authors = authorRepository.findAll()
                .stream()
                .map(AuthorMapper::toDto)
                .toList();
        return new LibraryResponse<>(authors);
    }

    @CacheEvict(value = "authors", key = "'all'")
    @Transactional
    public LibraryResponse<AuthorDTO> save(CreateAuthorRequest request) {
        authorHelper.ensureAuthorDoesNotExist(request.name());
        Author author = AuthorMapper.toEntity(request);
        Author saved = authorRepository.save(author);
        AuthorDTO dto = AuthorMapper.toDto(saved);
        return new LibraryResponse<>(dto);
    }

}
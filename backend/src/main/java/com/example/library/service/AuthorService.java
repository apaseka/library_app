package com.example.library.service;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.entity.Author;
import com.example.library.mapper.AuthorMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.util.AuthorHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorHelper authorHelper;
    private final AuthorMapper authorMapper;

    @Cacheable(value = "authors", key = "'all'")
    public LibraryResponse<List<AuthorDTO>> getAll() {
        log.debug("Cache miss for authors::all, loading authors from database");
        List<AuthorDTO> authors = authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
        log.debug("Loaded {} authors from database", authors.size());
        return new LibraryResponse<>(authors);
    }

    @CacheEvict(value = "authors", key = "'all'")
    @Transactional
    public LibraryResponse<AuthorDTO> save(CreateAuthorRequest request) {
        log.info("Creating author with name='{}' and evicting cache authors::all", request.name());

        authorHelper.ensureAuthorDoesNotExist(request.name());
        Author author = authorMapper.toEntity(request);
        Author saved = authorRepository.save(author);
        AuthorDTO dto = authorMapper.toDto(saved);

        log.info("Author created id={}, name='{}'", saved.getId(), saved.getName());
        return new LibraryResponse<>(dto);
    }

}
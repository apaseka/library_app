package com.example.library.service;

import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.entity.Author;
import com.example.library.mapper.AuthorMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.util.AuthorHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorHelper authorHelper;

    public List<AuthorDTO> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    @Transactional
    public AuthorDTO save(CreateAuthorRequest request) {
        authorHelper.ensureAuthorDoesNotExist(request.name());
        Author author = AuthorMapper.toEntity(request);
        Author saved = authorRepository.save(author);
        return AuthorMapper.toDto(saved);
    }

}
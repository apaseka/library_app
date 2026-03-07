package com.example.library.util;

import com.example.library.entity.Author;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorHelper {

    private final AuthorRepository authorRepository;

    public Author getAuthorIfExists(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Author \"%s\" not found", authorName)
                ));
    }

    public void ensureAuthorDoesNotExist(String authorName) {
        if (authorRepository.existsByName(authorName)) {
            throw new IllegalArgumentException(
                    String.format("Author \"%s\" already exists", authorName)
            );
        }
    }
}

package com.example.library.controller;

import com.example.library.dto.AuthorDTO;
import com.example.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDTO> getAll() {
        return authorService.getAll();
    }

    @PostMapping
    public AuthorDTO create(@RequestBody AuthorDTO dto) {
        return authorService.save(dto);
    }

}

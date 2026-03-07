package com.example.library.controller;

import com.example.library.dto.request.CreateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAll() {
        return bookService.getAll();
    }

    @PostMapping
    public BookDTO create(@Valid @RequestBody CreateBookRequest request) {
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    public BookDTO getBtId(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable Long id, @RequestBody BookDTO dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

}
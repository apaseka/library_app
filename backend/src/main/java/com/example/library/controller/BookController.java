package com.example.library.controller;

import com.example.library.annotation.ApiErrorResponses;
import com.example.library.dto.request.CreateUpdateBookRequest;
import com.example.library.dto.response.BookDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "API for working with books")
@ApiErrorResponses
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Receives all books from db")
    public LibraryResponse<List<BookDTO>> getAll() {
        log.debug("GET /books called");
        return bookService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adds book to db if author exists")
    @ApiResponse(
            responseCode = "201",
            description = "Book created successfully",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<BookDTO> create(@Valid @RequestBody CreateUpdateBookRequest request) {
        log.debug("POST /books called");
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Book found",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<BookDTO> getById(@PathVariable Long id) {
        log.debug("GET /books/{} called", id);
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change book by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Book updated successfully",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<BookDTO> update(@PathVariable Long id, @Valid @RequestBody CreateUpdateBookRequest request) {
        log.debug("PUT /books/{} called", id);
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    })
    public void delete(@PathVariable Long id) {
        log.debug("DELETE /books/{} called", id);
        bookService.deleteById(id);
    }

}
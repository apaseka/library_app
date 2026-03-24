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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books", description = "API for working with books")
@ApiErrorResponses
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Extracts all books from db")
    public LibraryResponse<List<BookDTO>> getAll() {
        return bookService.getAll();
    }

    @PostMapping
    @Operation(summary = "Adds book to db if author exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<BookDTO> create(@Valid @RequestBody CreateUpdateBookRequest request) {
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<BookDTO> getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<BookDTO> update(@PathVariable Long id, @Valid @RequestBody CreateUpdateBookRequest request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    })
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

}
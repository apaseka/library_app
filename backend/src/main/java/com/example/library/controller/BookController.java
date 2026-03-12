package com.example.library.controller;

import com.example.library.dto.request.CreateBookRequest;
import com.example.library.dto.response.BookDTO;
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
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Extracts all books from db")
    public List<BookDTO> getAll() {
        return bookService.getAll();
    }

    @PostMapping
    @Operation(summary = "Adds book to db if author exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Author not found in db")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}"))
    )
    public BookDTO create(@Valid @RequestBody CreateBookRequest request) {
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}"))
    )
    public BookDTO getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Get book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\":42,\"title\":\"Pride and Prejudice\",\"year\":1813,\"author\":\"Jane Austen\"}"))
    )
    public BookDTO update(@PathVariable Long id, @RequestBody BookDTO dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

}
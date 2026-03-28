package com.example.library.controller;

import com.example.library.annotation.ApiErrorResponses;
import com.example.library.dto.request.CreateAuthorRequest;
import com.example.library.dto.response.AuthorDTO;
import com.example.library.dto.response.LibraryResponse;
import com.example.library.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "API for working with authors")
@ApiErrorResponses
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "Receives all authors from db")
    @ApiResponse(
            responseCode = "200",
            description = "Authors returned successfully",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":[{\"id\":42,\"name\":\"Jane Austen\"}," +
                            "{\"id\":64,\"name\":\"James Joyce\"}]}")))
    public LibraryResponse<List<AuthorDTO>> getAll() {
        log.debug("GET /authors called");
        return authorService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adds the author, if it is not already in the database")
    @ApiResponse(
            responseCode = "201",
            description = "Authors returned successfully",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"name\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<AuthorDTO> create(@Valid @RequestBody CreateAuthorRequest request) {
        log.debug("POST /authors called");
        return authorService.save(request);
    }

}

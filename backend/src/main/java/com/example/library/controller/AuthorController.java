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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Authors", description = "API for working with authors")
@ApiErrorResponses
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "Extracts all authors from db")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors returned successfully")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":[{\"id\":42,\"name\":\"Jane Austen\"}," +
                            "{\"id\":64,\"name\":\"James Joyce\"}]}")))
    public LibraryResponse<List<AuthorDTO>> getAll() {
        return authorService.getAll();
    }

    @PostMapping
    @Operation(summary = "Adds the author, if it is not already in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors returned successfully")
    })
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"data\":{\"id\":42,\"name\":\"Jane Austen\"}}"))
    )
    public LibraryResponse<AuthorDTO> create(@Valid @RequestBody CreateAuthorRequest request) {
        return authorService.save(request);
    }

}

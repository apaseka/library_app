package com.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAuthorRequest(

        @NotBlank(message = "Author name must not be blank")
        @Size(max = 100, message = "Author name must be shorter than 100 characters")
        String name

) {}

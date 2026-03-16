package com.example.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUpdateBookRequest(

        @NotBlank(message = "Title must not be blank")
        @Size(max = 200, message = "Title must be shorter than 200 characters")
        String title,

        @NotNull(message = "Year must not be null")
        @Min(value = 0, message = "Year must be positive")
        Integer year,

        @NotBlank(message = "Author name must not be blank")
        String author

) {}

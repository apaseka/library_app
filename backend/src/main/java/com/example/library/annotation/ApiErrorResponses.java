package com.example.library.annotation;

import com.example.library.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
})
public @interface ApiErrorResponses {
}
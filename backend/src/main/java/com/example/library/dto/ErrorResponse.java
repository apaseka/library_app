package com.example.library.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        String error,
        String message,
        Instant timestamp,
        String path,
        Map<String, String> details
) {
}

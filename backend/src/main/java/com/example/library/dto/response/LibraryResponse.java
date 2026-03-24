package com.example.library.dto.response;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LibraryResponse<T> {
    private T data;
}

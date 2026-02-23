package com.elproducto.api.infrastructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    @Builder.Default
    private List<String> errors = Collections.emptyList();
    private Metadata metadata;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .errors(Collections.emptyList())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, Metadata metadata) {
        return ApiResponse.<T>builder()
                .data(data)
                .metadata(metadata)
                .errors(Collections.emptyList())
                .build();
    }

    public static <T> ApiResponse<T> error(String... errors) {
        return ApiResponse.<T>builder()
                .errors(List.of(errors))
                .build();
    }

    public static <T> ApiResponse<T> error(List<String> errors) {
        return ApiResponse.<T>builder()
                .errors(errors)
                .build();
    }
}

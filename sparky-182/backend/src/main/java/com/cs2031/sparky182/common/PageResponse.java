package com.cs2031.sparky182.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Same field names that Spring's Page uses (content, number, totalPages...),
 * but a stable, explicit JSON shape for the frontend.
 */
public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}

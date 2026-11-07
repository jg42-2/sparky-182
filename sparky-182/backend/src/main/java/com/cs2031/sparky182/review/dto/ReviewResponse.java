package com.cs2031.sparky182.review.dto;

import com.cs2031.sparky182.review.domain.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long albumId,
        int rating,
        String comment,
        String authorName,
        String authorEmail,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review r) {
        return new ReviewResponse(r.getId(), r.getAlbum().getId(), r.getRating(), r.getComment(),
                r.getUser().getName(), r.getUser().getEmail(), r.getCreatedAt());
    }
}

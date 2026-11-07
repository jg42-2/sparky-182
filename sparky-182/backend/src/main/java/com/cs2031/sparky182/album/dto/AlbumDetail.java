package com.cs2031.sparky182.album.dto;

public record AlbumDetail(
        Long id,
        String title,
        int year,
        String coverUrl,
        String lineup,
        long songCount,
        double averageRating,
        long reviewCount
) {}

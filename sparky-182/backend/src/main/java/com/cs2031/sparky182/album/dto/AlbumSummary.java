package com.cs2031.sparky182.album.dto;

import com.cs2031.sparky182.album.domain.Album;

public record AlbumSummary(Long id, String title, int year, String coverUrl, String lineup) {
    public static AlbumSummary from(Album a) {
        return new AlbumSummary(a.getId(), a.getTitle(), a.getYear(), a.getCoverUrl(), a.getLineup());
    }
}

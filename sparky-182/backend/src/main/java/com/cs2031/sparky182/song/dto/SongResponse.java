package com.cs2031.sparky182.song.dto;

import com.cs2031.sparky182.song.domain.Song;

public record SongResponse(Long id, String title, String duration, int trackNumber, Long albumId, String albumTitle) {
    public static SongResponse from(Song s) {
        return new SongResponse(s.getId(), s.getTitle(), s.getDuration(), s.getTrackNumber(),
                s.getAlbum().getId(), s.getAlbum().getTitle());
    }
}

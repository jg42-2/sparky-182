package com.cs2031.sparky182.song.infrastructure;

import com.cs2031.sparky182.song.domain.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByAlbumIdOrderByTrackNumberAsc(Long albumId);
    Page<Song> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    long countByAlbumId(Long albumId);
}

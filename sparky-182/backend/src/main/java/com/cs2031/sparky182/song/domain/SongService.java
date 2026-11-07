package com.cs2031.sparky182.song.domain;

import com.cs2031.sparky182.album.domain.AlbumService;
import com.cs2031.sparky182.common.PageResponse;
import com.cs2031.sparky182.exception.NotFoundException;
import com.cs2031.sparky182.song.dto.SongResponse;
import com.cs2031.sparky182.song.infrastructure.SongRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepo;
    private final AlbumService albumService;

    public SongService(SongRepository songRepo, AlbumService albumService) {
        this.songRepo = songRepo;
        this.albumService = albumService;
    }

    public List<SongResponse> findByAlbum(Long albumId) {
        albumService.getEntity(albumId); // 404 if the album does not exist
        return songRepo.findByAlbumIdOrderByTrackNumberAsc(albumId).stream()
                .map(SongResponse::from)
                .toList();
    }

    public PageResponse<SongResponse> search(String search, int page, int size) {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        if (size < 1 || size > 50) throw new IllegalArgumentException("size must be between 1 and 50");
        var pageable = PageRequest.of(page, size, Sort.by("id"));
        return PageResponse.from(
                songRepo.findByTitleContainingIgnoreCase(search, pageable).map(SongResponse::from)
        );
    }

    public Song getEntity(Long id) {
        return songRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Song not found: " + id));
    }
}

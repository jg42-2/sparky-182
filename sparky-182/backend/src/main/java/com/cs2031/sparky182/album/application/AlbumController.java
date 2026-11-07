package com.cs2031.sparky182.album.application;

import com.cs2031.sparky182.album.domain.AlbumService;
import com.cs2031.sparky182.album.dto.AlbumDetail;
import com.cs2031.sparky182.album.dto.AlbumSummary;
import com.cs2031.sparky182.song.domain.SongService;
import com.cs2031.sparky182.song.dto.SongResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final SongService songService;

    public AlbumController(AlbumService albumService, SongService songService) {
        this.albumService = albumService;
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumSummary>> getAlbums() {
        return ResponseEntity.ok(albumService.findAll());
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDetail> getAlbum(@PathVariable Long albumId) {
        return ResponseEntity.ok(albumService.findDetail(albumId));
    }

    @GetMapping("/{albumId}/songs")
    public ResponseEntity<List<SongResponse>> getSongs(@PathVariable Long albumId) {
        return ResponseEntity.ok(songService.findByAlbum(albumId));
    }
}

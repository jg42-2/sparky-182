package com.cs2031.sparky182.song.application;

import com.cs2031.sparky182.common.PageResponse;
import com.cs2031.sparky182.song.domain.SongService;
import com.cs2031.sparky182.song.dto.SongResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    // GET /api/songs?search=small&page=0&size=10
    @GetMapping
    public ResponseEntity<PageResponse<SongResponse>> search(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(songService.search(search.trim(), page, size));
    }
}

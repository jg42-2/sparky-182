package com.cs2031.sparky182.album.domain;

import com.cs2031.sparky182.album.dto.AlbumDetail;
import com.cs2031.sparky182.album.dto.AlbumSummary;
import com.cs2031.sparky182.album.infrastructure.AlbumRepository;
import com.cs2031.sparky182.exception.NotFoundException;
import com.cs2031.sparky182.review.infrastructure.ReviewRepository;
import com.cs2031.sparky182.song.infrastructure.SongRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepo;
    private final SongRepository songRepo;
    private final ReviewRepository reviewRepo;

    public AlbumService(AlbumRepository albumRepo, SongRepository songRepo, ReviewRepository reviewRepo) {
        this.albumRepo = albumRepo;
        this.songRepo = songRepo;
        this.reviewRepo = reviewRepo;
    }

    public List<AlbumSummary> findAll() {
        return albumRepo.findAll().stream()
                .sorted(Comparator.comparingInt(Album::getYear))
                .map(AlbumSummary::from)
                .toList();
    }

    public Album getEntity(Long id) {
        return albumRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Album not found: " + id));
    }

    public AlbumDetail findDetail(Long id) {
        Album a = getEntity(id);
        Double avg = reviewRepo.averageRatingByAlbumId(id);
        double rounded = avg == null ? 0.0 : Math.round(avg * 10) / 10.0;
        return new AlbumDetail(
                a.getId(), a.getTitle(), a.getYear(), a.getCoverUrl(), a.getLineup(),
                songRepo.countByAlbumId(id),
                rounded,
                reviewRepo.countByAlbumId(id)
        );
    }
}

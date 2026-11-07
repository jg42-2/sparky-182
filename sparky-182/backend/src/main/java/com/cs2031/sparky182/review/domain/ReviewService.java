package com.cs2031.sparky182.review.domain;

import com.cs2031.sparky182.album.domain.Album;
import com.cs2031.sparky182.album.domain.AlbumService;
import com.cs2031.sparky182.exception.ConflictException;
import com.cs2031.sparky182.exception.ForbiddenException;
import com.cs2031.sparky182.exception.NotFoundException;
import com.cs2031.sparky182.review.dto.ReviewResponse;
import com.cs2031.sparky182.review.infrastructure.ReviewRepository;
import com.cs2031.sparky182.user.domain.User;
import com.cs2031.sparky182.user.domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final AlbumService albumService;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepo, AlbumService albumService, UserService userService) {
        this.reviewRepo = reviewRepo;
        this.albumService = albumService;
        this.userService = userService;
    }

    public List<ReviewResponse> findByAlbum(Long albumId) {
        albumService.getEntity(albumId); // 404 if the album does not exist
        return reviewRepo.findByAlbumIdOrderByCreatedAtDesc(albumId).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse create(String email, Long albumId, int rating, String comment) {
        User user = userService.getByEmail(email);
        Album album = albumService.getEntity(albumId);
        if (reviewRepo.existsByUserIdAndAlbumId(user.getId(), albumId)) {
            throw new ConflictException("You already reviewed this album");
        }
        Review saved = reviewRepo.save(new Review(user, album, rating, comment.trim()));
        return ReviewResponse.from(saved);
    }

    @Transactional
    public void delete(String email, Long reviewId) {
        User user = userService.getByEmail(email);
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found: " + reviewId));
        boolean isAuthor = review.getUser().getId().equals(user.getId());
        if (!isAuthor && !user.isAdmin()) {
            throw new ForbiddenException("You can only delete your own reviews");
        }
        reviewRepo.delete(review);
    }
}

package com.cs2031.sparky182.review.application;

import com.cs2031.sparky182.review.domain.ReviewService;
import com.cs2031.sparky182.review.dto.ReviewResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    record ReviewRequest(
            @NotNull(message = "rating is required")
            @Min(value = 1, message = "rating must be between 1 and 5")
            @Max(value = 5, message = "rating must be between 1 and 5")
            Integer rating,
            @NotBlank(message = "comment is required")
            @Size(max = 280, message = "comment must be at most 280 characters")
            String comment) {}

    // Public
    @GetMapping("/albums/{albumId}/reviews")
    public ResponseEntity<List<ReviewResponse>> list(@PathVariable Long albumId) {
        return ResponseEntity.ok(reviewService.findByAlbum(albumId));
    }

    // Needs token. Answers 201 Created with the new review in the body.
    @PostMapping("/albums/{albumId}/reviews")
    public ResponseEntity<ReviewResponse> create(@AuthenticationPrincipal UserDetails principal,
                                                 @PathVariable Long albumId,
                                                 @Valid @RequestBody ReviewRequest req) {
        ReviewResponse created = reviewService.create(principal.getUsername(), albumId, req.rating(), req.comment());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Needs token. 204 No Content; 403 if the review is not yours (admins can delete any).
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails principal,
                                       @PathVariable Long reviewId) {
        reviewService.delete(principal.getUsername(), reviewId);
        return ResponseEntity.noContent().build();
    }
}

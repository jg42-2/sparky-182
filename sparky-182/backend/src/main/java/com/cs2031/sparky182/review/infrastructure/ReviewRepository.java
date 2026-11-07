package com.cs2031.sparky182.review.infrastructure;

import com.cs2031.sparky182.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAlbumIdOrderByCreatedAtDesc(Long albumId);
    boolean existsByUserIdAndAlbumId(Long userId, Long albumId);
    long countByAlbumId(Long albumId);

    @Query("select avg(r.rating) from Review r where r.album.id = :albumId")
    Double averageRatingByAlbumId(@Param("albumId") Long albumId);
}

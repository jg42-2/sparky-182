package com.cs2031.sparky182.review.domain;

import com.cs2031.sparky182.album.domain.Album;
import com.cs2031.sparky182.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "album_id"}))
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(nullable = false)
    private int rating; // 1..5

    @Column(name = "review_text", nullable = false, length = 280)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Review() {}

    public Review(User user, Album album, int rating, String comment) {
        this.user = user;
        this.album = album;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Album getAlbum() { return album; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

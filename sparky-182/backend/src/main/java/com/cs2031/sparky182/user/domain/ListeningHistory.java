package com.cs2031.sparky182.user.domain;

import com.cs2031.sparky182.album.domain.Album;
import jakarta.persistence.*;

@Entity
@Table(name = "listening_history",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "album_id"}))
public class ListeningHistory {

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
    private String status; // "escuchando" or "escuchado"

    public ListeningHistory() {}

    public ListeningHistory(User user, Album album, String status) {
        this.user = user;
        this.album = album;
        this.status = status;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Album getAlbum() { return album; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

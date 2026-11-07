package com.cs2031.sparky182.song.domain;

import com.cs2031.sparky182.album.domain.Album;
import jakarta.persistence.*;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String duration;

    private int trackNumber;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Song() {}

    public Song(String title, String duration, int trackNumber, Album album) {
        this.title = title;
        this.duration = duration;
        this.trackNumber = trackNumber;
        this.album = album;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public int getTrackNumber() { return trackNumber; }
    public Album getAlbum() { return album; }
}

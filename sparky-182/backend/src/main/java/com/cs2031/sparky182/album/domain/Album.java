package com.cs2031.sparky182.album.domain;

import jakarta.persistence.*;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "release_year") // "year" is a reserved word in H2
    private int year;

    private String coverUrl;

    private String lineup;

    public Album() {}

    public Album(String title, int year, String coverUrl, String lineup) {
        this.title = title;
        this.year = year;
        this.coverUrl = coverUrl;
        this.lineup = lineup;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getCoverUrl() { return coverUrl; }
    public String getLineup() { return lineup; }
}

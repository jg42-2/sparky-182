package com.cs2031.sparky182.tour.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

/** Fictional demo tour dates (NOT real blink-182 shows). */
@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String country;

    private String venue;

    @Column(name = "show_date") // "date" can clash with SQL keywords
    private LocalDate showDate;

    private int ticketsAvailable;

    private double price;

    public Tour() {}

    public Tour(String city, String country, String venue, LocalDate date, int ticketsAvailable, double price) {
        this.city = city;
        this.country = country;
        this.venue = venue;
        this.showDate = date;
        this.ticketsAvailable = ticketsAvailable;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getVenue() { return venue; }
    public LocalDate getShowDate() { return showDate; }
    public int getTicketsAvailable() { return ticketsAvailable; }
    public void setTicketsAvailable(int ticketsAvailable) { this.ticketsAvailable = ticketsAvailable; }
    public double getPrice() { return price; }
}

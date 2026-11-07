package com.cs2031.sparky182.tour.dto;

import com.cs2031.sparky182.tour.domain.Tour;

import java.time.LocalDate;

public record TourResponse(Long id, String city, String country, String venue,
                           LocalDate date, int ticketsAvailable, double price, boolean soldOut) {
    public static TourResponse from(Tour t) {
        return new TourResponse(t.getId(), t.getCity(), t.getCountry(), t.getVenue(),
                t.getShowDate(), t.getTicketsAvailable(), t.getPrice(), t.getTicketsAvailable() == 0);
    }
}

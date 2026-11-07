package com.cs2031.sparky182.tour.domain;

import com.cs2031.sparky182.common.PageResponse;
import com.cs2031.sparky182.exception.NotFoundException;
import com.cs2031.sparky182.tour.dto.TicketResponse;
import com.cs2031.sparky182.tour.dto.TourResponse;
import com.cs2031.sparky182.tour.infrastructure.TourRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class TourService {

    private final TourRepository tourRepo;

    public TourService(TourRepository tourRepo) {
        this.tourRepo = tourRepo;
    }

    public PageResponse<TourResponse> search(String city, int page, int size) {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        if (size < 1 || size > 50) throw new IllegalArgumentException("size must be between 1 and 50");
        var pageable = PageRequest.of(page, size, Sort.by("showDate"));
        return PageResponse.from(
                tourRepo.findByCityContainingIgnoreCase(city, pageable).map(TourResponse::from)
        );
    }

    public TourResponse findById(Long id) {
        return TourResponse.from(getEntity(id));
    }

    private Tour getEntity(Long id) {
        return tourRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour date not found: " + id));
    }

    @Transactional
    public TicketResponse buyTickets(Long tourId, int quantity) {
        Tour tour = getEntity(tourId);
        if (tour.getTicketsAvailable() < quantity) {
            throw new IllegalArgumentException("Not enough tickets: only " + tour.getTicketsAvailable() + " left");
        }
        tour.setTicketsAvailable(tour.getTicketsAvailable() - quantity);
        tourRepo.save(tour);
        return new TicketResponse(tour.getId(), tour.getCity(), quantity, tour.getTicketsAvailable());
    }

    // ---------- admin ----------

    public TourResponse create(String city, String country, String venue,
                               LocalDate date, int tickets, double price) {
        Tour saved = tourRepo.save(new Tour(city.trim(), country.trim(), venue.trim(), date, tickets, price));
        return TourResponse.from(saved);
    }

    public void delete(Long id) {
        tourRepo.delete(getEntity(id));
    }
}

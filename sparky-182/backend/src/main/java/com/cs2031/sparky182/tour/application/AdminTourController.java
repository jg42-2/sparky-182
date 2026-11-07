package com.cs2031.sparky182.tour.application;

import com.cs2031.sparky182.tour.domain.TourService;
import com.cs2031.sparky182.tour.dto.TourResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/** Only ROLE_ADMIN (see SecurityConfig). A normal user gets 403. */
@RestController
@RequestMapping("/api/admin/tours")
public class AdminTourController {

    private final TourService tourService;

    public AdminTourController(TourService tourService) {
        this.tourService = tourService;
    }

    record TourRequest(
            @NotBlank(message = "city is required") String city,
            @NotBlank(message = "country is required") String country,
            @NotBlank(message = "venue is required") String venue,
            @NotNull(message = "date is required") @Future(message = "date must be in the future") LocalDate date,
            @NotNull(message = "ticketsAvailable is required")
            @Min(value = 0, message = "ticketsAvailable must be >= 0") Integer ticketsAvailable,
            @NotNull(message = "price is required") @Positive(message = "price must be > 0") Double price) {}

    @PostMapping
    public ResponseEntity<TourResponse> create(@Valid @RequestBody TourRequest req) {
        TourResponse created = tourService.create(req.city(), req.country(), req.venue(),
                req.date(), req.ticketsAvailable(), req.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> delete(@PathVariable Long tourId) {
        tourService.delete(tourId);
        return ResponseEntity.noContent().build();
    }
}

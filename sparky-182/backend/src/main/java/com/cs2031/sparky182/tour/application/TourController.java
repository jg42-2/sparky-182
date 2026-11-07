package com.cs2031.sparky182.tour.application;

import com.cs2031.sparky182.common.PageResponse;
import com.cs2031.sparky182.tour.domain.TourService;
import com.cs2031.sparky182.tour.dto.TicketResponse;
import com.cs2031.sparky182.tour.dto.TourResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    record TicketRequest(
            @NotNull(message = "quantity is required")
            @Min(value = 1, message = "quantity must be between 1 and 6")
            @Max(value = 6, message = "quantity must be between 1 and 6")
            Integer quantity) {}

    // Public. GET /api/tours?city=li&page=0&size=5
    @GetMapping
    public ResponseEntity<PageResponse<TourResponse>> search(
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(tourService.search(city.trim(), page, size));
    }

    // Public
    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponse> getOne(@PathVariable Long tourId) {
        return ResponseEntity.ok(tourService.findById(tourId));
    }

    // Needs token
    @PostMapping("/{tourId}/tickets")
    public ResponseEntity<TicketResponse> buy(@PathVariable Long tourId,
                                              @Valid @RequestBody TicketRequest req) {
        return ResponseEntity.ok(tourService.buyTickets(tourId, req.quantity()));
    }
}

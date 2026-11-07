package com.cs2031.sparky182.tour.infrastructure;

import com.cs2031.sparky182.tour.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findByCityContainingIgnoreCase(String city, Pageable pageable);
}

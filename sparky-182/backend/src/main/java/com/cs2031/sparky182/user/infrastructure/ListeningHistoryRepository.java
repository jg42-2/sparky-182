package com.cs2031.sparky182.user.infrastructure;

import com.cs2031.sparky182.user.domain.ListeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {
    Optional<ListeningHistory> findByUserIdAndAlbumId(Long userId, Long albumId);
    void deleteByUserIdAndAlbumId(Long userId, Long albumId);
}

package com.cs2031.sparky182.album.infrastructure;

import com.cs2031.sparky182.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {}

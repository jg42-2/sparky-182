package com.cs2031.sparky182.user.domain;

import com.cs2031.sparky182.album.domain.Album;
import com.cs2031.sparky182.album.domain.AlbumService;
import com.cs2031.sparky182.exception.ConflictException;
import com.cs2031.sparky182.song.domain.Song;
import com.cs2031.sparky182.song.domain.SongService;
import com.cs2031.sparky182.user.dto.MeResponse;
import com.cs2031.sparky182.user.infrastructure.ListeningHistoryRepository;
import com.cs2031.sparky182.user.infrastructure.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final ListeningHistoryRepository historyRepo;
    private final SongService songService;
    private final AlbumService albumService;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo,
                       ListeningHistoryRepository historyRepo,
                       SongService songService,
                       AlbumService albumService,
                       PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.historyRepo = historyRepo;
        this.songService = songService;
        this.albumService = albumService;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public User getByEmail(String email) {
        return (User) loadUserByUsername(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User register(String email, String password, String name) {
        if (userRepo.existsByEmail(email)) {
            throw new ConflictException("Email already in use");
        }
        return userRepo.save(new User(email, encoder.encode(password), name, "USER"));
    }

    // ---------- profile ----------

    public MeResponse me(String email) {
        return MeResponse.from(getByEmail(email));
    }

    @Transactional
    public MeResponse updateName(String email, String name) {
        User user = getByEmail(email);
        user.setName(name.trim());
        return MeResponse.from(userRepo.save(user));
    }

    // ---------- favorites (songs) — same as Sparky The Horizon ----------

    @Transactional
    public void addFavorite(String email, Long songId) {
        User user = getByEmail(email);
        Song song = songService.getEntity(songId);
        user.getFavorites().add(song);
        userRepo.save(user);
    }

    @Transactional
    public void removeFavorite(String email, Long songId) {
        User user = getByEmail(email);
        user.getFavorites().removeIf(s -> s.getId().equals(songId));
        userRepo.save(user);
    }

    // ---------- listening history (albums) — same idea as SparkyRoll ----------

    @Transactional
    public void upsertHistory(String email, Long albumId, String status) {
        User user = getByEmail(email);
        Album album = albumService.getEntity(albumId);
        historyRepo.findByUserIdAndAlbumId(user.getId(), albumId)
                .ifPresentOrElse(
                        h -> h.setStatus(status),
                        () -> historyRepo.save(new ListeningHistory(user, album, status))
                );
    }

    @Transactional
    public void removeHistory(String email, Long albumId) {
        User user = getByEmail(email);
        historyRepo.deleteByUserIdAndAlbumId(user.getId(), albumId);
    }
}

package com.cs2031.sparky182.user.application;

import com.cs2031.sparky182.user.domain.UserService;
import com.cs2031.sparky182.user.dto.MeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Everything here needs "Authorization: Bearer <token>".
 * Note: there is NO "GET /api/user/favorites" nor "GET /api/user/history"
 * (same as the course PCs): the frontend has to remember them itself.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    record SongIdRequest(@NotNull(message = "songId is required") Long songId) {}

    record AlbumIdRequest(@NotNull(message = "albumId is required") Long albumId) {}

    record HistoryRequest(
            @NotNull(message = "albumId is required") Long albumId,
            @NotBlank(message = "status is required")
            @Pattern(regexp = "escuchando|escuchado", message = "status must be 'escuchando' or 'escuchado'")
            String status) {}

    record UpdateMeRequest(
            @NotBlank(message = "name is required")
            @Size(max = 60, message = "name must be at most 60 characters")
            String name) {}

    // ---------- profile ----------

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(userService.me(principal.getUsername()));
    }

    @PatchMapping("/me")
    public ResponseEntity<MeResponse> updateMe(@AuthenticationPrincipal UserDetails principal,
                                               @Valid @RequestBody UpdateMeRequest req) {
        return ResponseEntity.ok(userService.updateName(principal.getUsername(), req.name()));
    }

    // ---------- favorites ----------

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal UserDetails principal,
                                            @Valid @RequestBody SongIdRequest req) {
        userService.addFavorite(principal.getUsername(), req.songId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal UserDetails principal,
                                               @Valid @RequestBody SongIdRequest req) {
        userService.removeFavorite(principal.getUsername(), req.songId());
        return ResponseEntity.ok().build();
    }

    // ---------- listening history ----------

    @PostMapping("/history")
    public ResponseEntity<Void> upsertHistory(@AuthenticationPrincipal UserDetails principal,
                                              @Valid @RequestBody HistoryRequest req) {
        userService.upsertHistory(principal.getUsername(), req.albumId(), req.status());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> removeHistory(@AuthenticationPrincipal UserDetails principal,
                                              @Valid @RequestBody AlbumIdRequest req) {
        userService.removeHistory(principal.getUsername(), req.albumId());
        return ResponseEntity.ok().build();
    }
}

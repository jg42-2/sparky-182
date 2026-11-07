package com.cs2031.sparky182.user.dto;

import com.cs2031.sparky182.user.domain.User;

public record MeResponse(Long id, String name, String email, String role) {
    public static MeResponse from(User u) {
        return new MeResponse(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}

package com.cs2031.sparky182.auth.application;

import com.cs2031.sparky182.auth.domain.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    record RegisterRequest(
            @NotBlank(message = "email is required") @Email(message = "email is not valid") String email,
            @NotBlank(message = "password is required")
            @Size(min = 6, message = "password must have at least 6 characters") String password,
            @NotBlank(message = "name is required") String name) {}

    record LoginRequest(
            @NotBlank(message = "email is required") @Email(message = "email is not valid") String email,
            @NotBlank(message = "password is required") String password) {}

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest req) {
        String token = authService.register(req.email(), req.password(), req.name());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req.email(), req.password());
        return ResponseEntity.ok(Map.of("token", token));
    }
}

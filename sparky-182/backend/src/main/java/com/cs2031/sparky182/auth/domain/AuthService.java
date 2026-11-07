package com.cs2031.sparky182.auth.domain;

import com.cs2031.sparky182.exception.UnauthorizedException;
import com.cs2031.sparky182.security.JwtUtil;
import com.cs2031.sparky182.user.domain.User;
import com.cs2031.sparky182.user.domain.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(String email, String password, String name) {
        User user = userService.register(email.trim().toLowerCase(), password, name.trim());
        return jwtUtil.generate(user.getEmail());
    }

    public String login(String email, String password) {
        User user = userService.findByEmail(email.trim().toLowerCase())
                .filter(u -> encoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        return jwtUtil.generate(user.getEmail());
    }
}

package com.cs2031.sparky182.exception;

/** Answered with HTTP 401 and body {"error": message}. */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

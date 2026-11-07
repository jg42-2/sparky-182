package com.cs2031.sparky182.exception;

/** Answered with HTTP 404 and body {"error": message}. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

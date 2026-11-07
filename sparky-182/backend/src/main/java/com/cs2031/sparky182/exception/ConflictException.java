package com.cs2031.sparky182.exception;

/** Answered with HTTP 409 and body {"error": message}. */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}

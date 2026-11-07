package com.cs2031.sparky182.exception;

/** Answered with HTTP 403 and body {"error": message}. */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}

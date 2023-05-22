package BugTracker.bugket.controllers;

import java.time.LocalDateTime;

public class ErrorHandler {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public ErrorHandler(String message) {
        this.message = message;
    }
}
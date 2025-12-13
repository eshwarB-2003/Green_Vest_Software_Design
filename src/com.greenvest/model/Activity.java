package com.greenvest.model;
import java.time.LocalDateTime;

public class Activity {

    private String message;
    private String performedBy;
    private LocalDateTime timestamp;

    public Activity(String message, String performedBy) {
        this.message = message;
        this.performedBy = performedBy;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

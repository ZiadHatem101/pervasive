package com.example.pervasiveproj;

public class LogActivityItem {
    private Long timestamp; // Attribute for the timestamp
    private String message;    // Attribute for the text

    // Constructor
    public LogActivityItem(Long timestamp, String text) {
        this.timestamp = timestamp;
        this.message = text;

    }

    // Getter for timestamp
    public Long getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // Getter for text
    public String getText() {
        return message;
    }

    // Setter for text
    public void setText(String text) {
        this.message = text;
    }

    // Optionally, override toString() for easier debugging or display
    @Override
    public String toString() {
        return "LogActivityItem{" +
                "timestamp=" + timestamp +
                ", text='" + message + '\'' +
                '}';
    }
}

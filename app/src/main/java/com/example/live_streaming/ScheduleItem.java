package com.example.live_streaming;

public class ScheduleItem {
    private String subject;
    private String time;

    public ScheduleItem(String subject, String time) {
        this.subject = subject;
        this.time = time;
    }

    // Getters
    public String getSubject() { return subject; }
    public String getTime() { return time; }

    // Setters
    public void setSubject(String subject) { this.subject = subject; }
    public void setTime(String time) { this.time = time; }
}

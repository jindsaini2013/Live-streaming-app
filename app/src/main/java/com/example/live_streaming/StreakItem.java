package com.example.live_streaming;

public class StreakItem {
    private String title;
    private String value;
    private String color;

    public StreakItem(String title, String value, String color) {
        this.title = title;
        this.value = value;
        this.color = color;
    }

    // Getters
    public String getTitle() { return title; }
    public String getValue() { return value; }
    public String getColor() { return color; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setValue(String value) { this.value = value; }
    public void setColor(String color) { this.color = color; }
}

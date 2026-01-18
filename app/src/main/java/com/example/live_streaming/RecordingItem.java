package com.example.live_streaming;

public class RecordingItem {
    private String title;
    private String chapter;
    private String duration;
    private float progress;
    private String videoPath;
    private String date;

    public RecordingItem(String title, String chapter, String duration,
                         float progress, String videoPath, String date) {
        this.title = title;
        this.chapter = chapter;
        this.duration = duration;
        this.progress = progress;
        this.videoPath = videoPath;
        this.date = date;
    }

    // Getters
    public String getTitle() { return title; }
    public String getChapter() { return chapter; }
    public String getDuration() { return duration; }
    public float getProgress() { return progress; }
    public String getVideoPath() { return videoPath; }
    public String getDate() { return date; }
}

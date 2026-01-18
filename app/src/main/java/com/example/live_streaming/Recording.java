package com.example.live_streaming;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recording")
public class Recording {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String subject;
    private String chapter;
    private String duration;
    private String recordingPath;
    private String thumbnailPath;
    private long timestamp;
    private String teacherName;
    private String date;

    public Recording(String title, String subject, String chapter, String duration,
                     String recordingPath, String thumbnailPath, long timestamp,
                     String teacherName, String date) {
        this.title = title;
        this.subject = subject;
        this.chapter = chapter;
        this.duration = duration;
        this.recordingPath = recordingPath;
        this.thumbnailPath = thumbnailPath;
        this.timestamp = timestamp;
        this.teacherName = teacherName;
        this.date = date;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getRecordingPath() { return recordingPath; }
    public void setRecordingPath(String recordingPath) { this.recordingPath = recordingPath; }
    public String getThumbnailPath() { return thumbnailPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public float getProgress() { return 0.0f; } // Default progress
}

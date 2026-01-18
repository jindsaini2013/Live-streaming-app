package com.example.live_streaming;

public class TestItem {
    private String name;
    private int questionCount;
    private String duration;
    private boolean isCompleted;

    public TestItem() {}

    public TestItem(String name, int questionCount, String duration, boolean isCompleted) {
        this.name = name;
        this.questionCount =questionCount;
        this.duration = duration;
        this.isCompleted = isCompleted;
    }

    // Getters
    public String getName() { return name; }
    public String getQuestionCount() { return questionCount + " Questions"; }
    public String getDuration() { return duration; }
    public boolean isCompleted() { return isCompleted; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}

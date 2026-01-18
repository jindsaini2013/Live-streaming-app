package com.example.live_streaming;
public class Student {
    private String id;
    private String name;
    private String email;
    private AttendanceStatus attendanceStatus;

    public Student(String id, String name, String email, AttendanceStatus attendanceStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.attendanceStatus = attendanceStatus;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public AttendanceStatus getAttendanceStatus() { return attendanceStatus; }

    public void setAttendanceStatus(AttendanceStatus status) {
        this.attendanceStatus = status;
    }
}

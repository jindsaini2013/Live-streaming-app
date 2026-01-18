// Quiz.java
package com.example.live_streaming;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz")
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String testName;
    public String subject;
    public String question;
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public String correctAnswer;
}

package com.example.live_streaming;
// QuizDao.java
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    void insertQuiz(Quiz quiz);
    @Query("SELECT DISTINCT subject FROM quiz")
    List<String> getAllSubjects();

    // Get tests for subject: gives test name and question count for tabs
    @Query("SELECT testName AS name, COUNT(*) AS questionCount, '15 min' AS duration, 0 AS isCompleted " +
            "FROM quiz WHERE subject = :subject GROUP BY testName")
    List<TestItem> getDistinctTestsForSubject(String subject);

    // Get all quiz questions for a particular test (when a test is selected)
    @Query("SELECT * FROM quiz WHERE subject = :subject AND testName = :testName")
    List<Quiz> getQuizzesForTest(String subject, String testName);
}

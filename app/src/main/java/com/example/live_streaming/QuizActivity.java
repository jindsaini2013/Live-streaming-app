package com.example.live_streaming;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private RadioGroup rgOptions;
    private Button btnSubmit;
    private String testName;
    private int correctAnswers;
    private String subject;
    private List<Quiz> questions;
    private int currentIndex = 0;
    private int totalQuestions;
    private boolean isComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        testName = getIntent().getStringExtra("test_name");
        subject = getIntent().getStringExtra("subject");
        //totalQuestions=getIntent().getIntExtra("questions",0);
        isComplete=getIntent().getBooleanExtra("complete",false);


        initViews();
        loadQuiz(testName, subject);
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tv_question);
        rgOptions = findViewById(R.id.rg_options);
        btnSubmit = findViewById(R.id.btn_submit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(testName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void loadQuiz(String testName, String subject) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            questions = db.quizDao().getQuizzesForTest(subject, testName);

            runOnUiThread(this::displayQuestion);
        }).start();
    }

    private void displayQuestion() {
        if (currentIndex >= questions.size()) {
            //Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
            showResults();
            isComplete=true;
            return;
        }
        Quiz q = questions.get(currentIndex);
        tvQuestion.setText(q.question);
        rgOptions.removeAllViews();
        String[] opts = {q.optionA, q.optionB, q.optionC, q.optionD};
        for (int i = 0; i < opts.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opts[i]);
            rb.setId(i);
            rgOptions.addView(rb);
        }
        btnSubmit.setOnClickListener(v -> checkAnswer(q));
    }
    private void checkAnswer(Quiz q) {

        int selectedId = rgOptions.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;}
            char selectedChar = (char) ('A' + selectedId);
            String selected = String.valueOf(selectedChar);
            String correctAnswer = q.correctAnswer.trim();
            Log.d("AnswerCheck", "Selected answer: '" + selected + "'");
            Log.d("AnswerCheck", "Stored correctAnswer: '" + correctAnswer + "'");
            if (selected.equalsIgnoreCase(correctAnswer)) {
                correctAnswers++;
                totalQuestions++;
                Toast.makeText(this, "Correct! ✅", Toast.LENGTH_SHORT).show();
            } else {
                totalQuestions++;
                Toast.makeText(this, "Incorrect ❌", Toast.LENGTH_SHORT).show();
            }
            currentIndex++;
            displayQuestion();

    }
    private void showResults() {
        String msg = "You scored " + correctAnswers + " out of " + totalQuestions;
        new AlertDialog.Builder(this)
                .setTitle("Quiz Completed")
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

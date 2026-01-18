package com.example.live_streaming;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TeacherQuizUploadActivity extends AppCompatActivity {

    private EditText  etQuestion, etOptionA, etOptionB, etOptionC, etOptionD,etTotalQuestions;
    private Spinner etTestName,spinnerSubject;
    private RadioGroup rgCorrectAnswer;
    private TextView tvQuestionNumber;

    private Button btnNext, btnStart, btnCancel;
    private int totalQuestions, currentQuestionIndex = 0;

    private List<Quiz> quizBuffer = new ArrayList<>();
    private String testName;
    private String subject;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_upload_quiz);

        // Initialize views: including etTestName, spinnerSubject, total question input etc.
        etTestName = findViewById(R.id.et_test_name);
        spinnerSubject = findViewById(R.id.spinner_subject);
        etTotalQuestions = findViewById(R.id.et_total_questions);
        etQuestion = findViewById(R.id.et_question);
        etOptionA = findViewById(R.id.et_option_a);
        etOptionB = findViewById(R.id.et_option_b);
        etOptionC = findViewById(R.id.et_option_c);
        etOptionD = findViewById(R.id.et_option_d);
        rgCorrectAnswer = findViewById(R.id.rg_correct_answer);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        btnStart = findViewById(R.id.btn_start_quiz);
        btnNext = findViewById(R.id.btn_next_question);
        btnCancel = findViewById(R.id.btn_cancel);
        spinnerSubject = findViewById(R.id.spinner_subject);

        ArrayAdapter<String> testNameAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Test 1", "Test 2", "Test 3"}); // Replace with actual or dynamic test names
        testNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etTestName.setAdapter(testNameAdapter);

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Math", "Science", "English", "History"}); // your subjects
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        btnStart.setOnClickListener(v -> {
            testName = etTestName.getSelectedItem().toString();
            totalQuestions= Integer.parseInt(etTotalQuestions.getText().toString());

            subject = spinnerSubject.getSelectedItem().toString();

            Object selectedItem = spinnerSubject.getSelectedItem();
            if (selectedItem == null) {
                Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show();
                return;
            }
            subject = selectedItem.toString();

            if (totalQuestions==0 ) {
                Toast.makeText(this, "Please enter test name and valid number of questions", Toast.LENGTH_SHORT).show();
                return;
            }


            totalQuestions = Integer.parseInt(etTotalQuestions.getText().toString());
            currentQuestionIndex = 0;
            //quizBuffer.clear();
            Log.d("TeacherQuizUpload", "Start button clicked");
            // Hide initial inputs, show question inputs
            showQuestionInputLayout();
            loadQuestionForm();
        });

        btnNext.setOnClickListener(v -> {
            if (validateQuestionInput()) {
                saveCurrentQuestion();
                currentQuestionIndex++;
                if (currentQuestionIndex < totalQuestions) {
                    loadQuestionForm();
                } else {
                    saveAllQuestionsToDatabase();
                    Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(view -> {
            Intent i=new Intent(TeacherQuizUploadActivity.this,TeacherMainActivity.class);
            startActivity(i);
        });
    }
    private void showQuestionInputLayout() {
        // Assuming your XML has two layouts for steps: initial input and question input
        findViewById(R.id.layout_initial_inputs).setVisibility(View.GONE);
        findViewById(R.id.layout_question_input).setVisibility(View.VISIBLE);
    }

    private void clearQuestionInputs() {
        ((EditText) findViewById(R.id.et_question)).setText("");
        ((EditText) findViewById(R.id.et_option_a)).setText("");
        ((EditText) findViewById(R.id.et_option_b)).setText("");
        ((EditText) findViewById(R.id.et_option_c)).setText("");
        ((EditText) findViewById(R.id.et_option_d)).setText("");
        ((RadioGroup) findViewById(R.id.rg_correct_answer)).clearCheck();
    }


    private void loadQuestionForm() {
        clearQuestionInputs();
        tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + totalQuestions);
    }

    private void saveCurrentQuestion() {
        Quiz q = new Quiz();
        q.testName = testName;
        q.subject = subject;
        q.question = etQuestion.getText().toString().trim();
        q.optionA = etOptionA.getText().toString().trim();
        q.optionB = etOptionB.getText().toString().trim();
        q.optionC = etOptionC.getText().toString().trim();
        q.optionD = etOptionD.getText().toString().trim();

        // Get correct answer from rgCorrectAnswer (RadioButton selected text)
        int checkedId = rgCorrectAnswer.getCheckedRadioButtonId();
        RadioButton checkedButton = findViewById(checkedId);
        q.correctAnswer = checkedButton.getText().toString().trim();

        quizBuffer.add(q);
    }

    private void saveAllQuestionsToDatabase() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            for (Quiz q : quizBuffer) {
                db.quizDao().insertQuiz(q);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Quiz uploaded successfully", Toast.LENGTH_LONG).show();
                finish();
            });
        }).start();
    }

    private boolean validateQuestionInput() {
        // Validate none of the fields are empty and correct answer selected
        // Show Toast for missing fields
        return true;
    }
}

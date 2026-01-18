package com.example.live_streaming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TeacherMainActivity extends AppCompatActivity {

    private CardView cardAttendance, cardLiveStreaming,cardUploadQuiz,card_manage_students;
    private SharedPreferences sharedPreferences;
    private Button buttonlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        initViews();
        setupClickListeners();

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Teacher: " + getCurrentUsername());
        }
    }

    private void initViews() {
        cardAttendance = findViewById(R.id.card_attendance);
        cardLiveStreaming = findViewById(R.id.card_live_streaming);
        buttonlogout=findViewById(R.id.btn_logout);
        cardUploadQuiz = findViewById(R.id.card_quiz_upload);
        card_manage_students=findViewById(R.id.card_manage_students);
    }

    private void setupClickListeners() {
        cardAttendance.setOnClickListener(v -> {
            startActivity(new Intent(this, AttendanceActivity.class));
        });

        cardLiveStreaming.setOnClickListener(v -> {
            // Your existing MainActivity for live streaming
            startActivity(new Intent(this, MainActivity.class));
        });
        cardUploadQuiz.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherQuizUploadActivity.class));
        });
        card_manage_students.setOnClickListener(v -> {
            startActivity(new Intent(this, Register.class));
        });

        buttonlogout.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private String getCurrentUsername() {
        return sharedPreferences.getString("username", "Teacher");
    }

    // Add options menu for logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        // Clear saved login data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



}

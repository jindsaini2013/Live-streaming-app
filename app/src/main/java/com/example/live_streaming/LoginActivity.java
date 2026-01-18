package com.example.live_streaming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Import this if you are using TextInputEditText from Material Design
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Using TextInputEditText to match your XML
    private TextInputEditText etEmail, etPassword;
    private RadioGroup rgUserType;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This layout name must match your XML file name
        setContentView(R.layout.activity_login);

        initViews();

        db = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        createTestUsers();

        if (isUserLoggedIn()) {
            redirectToRoleBasedActivity();
        }

        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void initViews() {
        // Using the exact IDs from your activity_login.xml
        etEmail = findViewById(R.id.et_login_user);
        etPassword = findViewById(R.id.et_login_Password);
        rgUserType = findViewById(R.id.rg_user_role);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        int selectedRoleId = rgUserType.getCheckedRadioButtonId();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedRoleId == -1) {
            // This should not happen if one is checked by default, but good to have
            Toast.makeText(this, "Please select a role (Student/Teacher)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the text from the selected RadioButton ("Student" or "Teacher")
        RadioButton selectedRadioButton = findViewById(selectedRoleId);
        String role = selectedRadioButton.getText().toString();

        // --- This is the new logic ---
        // We call the updated checkUser method that validates all 3 fields
        boolean isValidUser = db.checkUser(email, password, role);

        if (isValidUser) {
            // Convert "Student" to "student" for saving
            String userTypeRole = role.toLowerCase();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("username", email);
            editor.putString("userType", userTypeRole);
            editor.apply();

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            redirectToRoleBasedActivity();
        } else {
            // This is the toast you requested
            Toast.makeText(this, "One of the fields is wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void redirectToRoleBasedActivity() {
        String userType = sharedPreferences.getString("userType", "");

        Intent intent;
        if ("student".equals(userType)) {
            // Make sure you have a StudentMainActivity.class
            intent = new Intent(this, StudentMainActivity.class);
        } else if ("teacher".equals(userType)) {
            // Make sure you have a TeacherMainActivity.class
            intent = new Intent(this, TeacherMainActivity.class);
        } else {
            // Fallback just in case
            return;
        }

        startActivity(intent);
        finish();
    }
    private void createTestUsers() {
        // Check if users already exist to avoid duplicates
        if (!db.checkUser("student@test.com", "student123", "Student")) {
            db.insertData("student@test.com", "student123", "Student");
            Toast.makeText(this, "Test student created", Toast.LENGTH_SHORT).show();
        }

        if (!db.checkUser("teacher@test.com", "teacher123", "Teacher")) {
            db.insertData("teacher@test.com", "teacher123", "Teacher");
            Toast.makeText(this, "Test teacher created", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Disabling back button on login screen
        // super.onBackPressed();
        super.onBackPressed();
    }
}


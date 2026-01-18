package com.example.live_streaming;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log; // Import Log for better debugging
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    // Declare all the components from the register.xml layout
    private EditText etEmail, etPassword,etID;
    private RadioGroup rgUserType;
    private RadioButton rbStudent, rbTeacher;
    private Button btnRegister, btnUpdate, btnDelete, btnViewAll;
    DatabaseHelper db;

    // It's good practice to define a TAG for logging
    private static final String TAG = "RegisterActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        try {
            // Initialize all the components
            etID = findViewById(R.id.et_id);
            etEmail = findViewById(R.id.et_register_email);
            etPassword = findViewById(R.id.et_register_password);
            rgUserType = findViewById(R.id.rg_register_user_type);
            rbStudent = findViewById(R.id.rb_register_student);
            rbTeacher = findViewById(R.id.rb_register_teacher);
            btnRegister = findViewById(R.id.btn_register);
            btnUpdate = findViewById(R.id.btn_update);
            btnDelete = findViewById(R.id.btn_delete);
            btnViewAll = findViewById(R.id.btn_view_all);

            // Initialize DatabaseHelper
            db = new DatabaseHelper(getApplicationContext());

            // --- Set Click Listeners ---
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUserData();
                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUserData();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUserData();
                }
            });
            btnViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewUserData();
                }
            });

        } catch (Exception e) {
            // If findViewById or anything else fails, log it and show a toast
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error initializing page: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void saveUserData() {
        try {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Basic validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = ((RadioButton) findViewById(rgUserType.getCheckedRadioButtonId())).getText().toString();
            boolean isInserted = db.insertData(email, password, role);

            if (isInserted) {
                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                Toast.makeText(getApplicationContext(), "Error: Could not register user", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // Catch any unexpected errors (like database errors)
            Log.e(TAG, "Error saving user: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error saving: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateUserData() {
        try {
            String id = etID.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Basic validation
            if (id.isEmpty()) {
                Toast.makeText(getApplicationContext(), "ID is required to update", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = ((RadioButton) findViewById(rgUserType.getCheckedRadioButtonId())).getText().toString();
            boolean isUpdated = db.updateData(id, email, password, role);

            if (isUpdated) {
                Toast.makeText(getApplicationContext(), "User Updated successfully", Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                Toast.makeText(getApplicationContext(), "Error: Could not update user (ID might not exist)", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating user: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error updating: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteUserData() {
        try {
            String id = etID.getText().toString().trim();

            // Basic validation
            if (id.isEmpty()) {
                Toast.makeText(getApplicationContext(), "ID is required to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer deletedRows = db.deleteData(id);

            if (deletedRows > 0) {
                Toast.makeText(getApplicationContext(), "User Deleted successfully", Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                Toast.makeText(getApplicationContext(), "Error: Could not delete user (ID might not exist)", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting user: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error deleting: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void viewUserData() {
        try {
            Cursor res = db.getAllData();
            if (res == null) {
                showData("Error", "Could not retrieve data");
                return;
            }
            if (res.getCount() == 0) {
                showData("No Data", "No users found in database");
                res.close(); // Always close the cursor
                return;
            }

            StringBuilder builder = new StringBuilder();
            while (res.moveToNext()) {
                builder.append("ID: ").append(res.getString(0)).append("\n");
                builder.append("Email: ").append(res.getString(1)).append("\n");
                builder.append("Password: ").append(res.getString(2)).append("\n");
                builder.append("Role: ").append(res.getString(3)).append("\n\n");
            }
            res.close(); // Always close the cursor after use

            // *** MOVED THIS CALL ***
            // Show data *after* the loop is finished
            showData("All Users", builder.toString());

        } catch (Exception e) {
            Log.e(TAG, "Error viewing data: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error viewing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showData(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    // Helper method to clear text fields
    private void clearFields() {
        etID.setText("");
        etEmail.setText("");
        etPassword.setText("");
        rbStudent.setChecked(true); // Reset to default
    }
}


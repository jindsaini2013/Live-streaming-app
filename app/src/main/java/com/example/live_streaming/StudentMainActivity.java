package com.example.live_streaming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabChat;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hi " + getCurrentUsername() + "!");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabChat = findViewById(R.id.fab_chat);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        setupBottomNavigation();
        setupFabChat();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_tests) {
                selectedFragment = new TestsFragment();
            } else if (item.getItemId() == R.id.nav_recordings) {
                selectedFragment = new RecordingsFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void setupFabChat() {
        fabChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private String getCurrentUsername() {
        return sharedPreferences.getString("username", "Student");
    }

    // Inflate the toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handle toolbar menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_profile_view) {
            showProfileDialog();
            return true;
        } else if (itemId == R.id.action_settings) {
            showSettingsDialog();
            return true;
        } else if (itemId == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProfileDialog() {
        String username = getCurrentUsername();
        String userType = sharedPreferences.getString("userType", "student");

        new AlertDialog.Builder(this)
                .setTitle("Profile Information")
                .setMessage("Username: " + username + "\nRole: " + userType.substring(0, 1).toUpperCase() + userType.substring(1))
                .setPositiveButton("OK", null)
                .setNeutralButton("Edit Profile", (dialog, which) -> {
                    Toast.makeText(this, "Edit Profile functionality coming soon!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showSettingsDialog() {
        String[] options = {"Notifications", "Theme", "Language", "Privacy"};

        new AlertDialog.Builder(this)
                .setTitle("Settings")
                .setItems(options, (dialog, which) -> {
                    Toast.makeText(this, "Selected: " + options[which], Toast.LENGTH_SHORT).show();

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_logout)
                .show();
    }

    private void performLogout() {
        // Clear saved login data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to login activity and clear back stack
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

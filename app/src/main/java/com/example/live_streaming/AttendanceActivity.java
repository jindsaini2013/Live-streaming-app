package com.example.live_streaming;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private TextView tvSelectedDate, tvTotalStudents, tvPresentCount, tvAbsentCount;
    private Button btnSelectDate, btnSaveAttendance;
    private FloatingActionButton fabSelectAll;
    private RecyclerView rvStudents;
    private AttendanceAdapter attendanceAdapter;
    private List<Student> studentList;
    private Calendar selectedCalendar;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        db = new DatabaseHelper(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupButtons();
        loadStudents();
        updateSelectedDate();
        updateAttendanceStats();
    }

    private void initViews() {
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvTotalStudents = findViewById(R.id.tv_total_students);
        tvPresentCount = findViewById(R.id.tv_present_count);
        tvAbsentCount = findViewById(R.id.tv_absent_count);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSaveAttendance = findViewById(R.id.btn_save_attendance);
        fabSelectAll = findViewById(R.id.fab_select_all);
        rvStudents = findViewById(R.id.rv_students);

        selectedCalendar = Calendar.getInstance();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Attendance Management");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        attendanceAdapter = new AttendanceAdapter(new ArrayList<>(), this::updateAttendanceStats);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(attendanceAdapter);
    }

    private void setupButtons() {
        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());

        btnSaveAttendance.setOnClickListener(v -> saveAttendance());

        fabSelectAll.setOnClickListener(v -> markAllPresent());
    }

    private void loadStudents() {
        // Load students from database instead of hardcoded samples
        studentList = db.getStudentList();

        if (studentList.isEmpty()) {
            Toast.makeText(this, "No students found in database", Toast.LENGTH_SHORT).show();
        }

        attendanceAdapter.updateStudents(studentList);
        updateAttendanceStats();
    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(year, month, dayOfMonth);
                    updateSelectedDate();
                    // You could load attendance data for this date here
                    loadAttendanceForDate();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void updateSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        tvSelectedDate.setText(sdf.format(selectedCalendar.getTime()));
    }

    private void loadAttendanceForDate() {
        // In a real app, you would load attendance data from database for the selected date
        // For now, we'll just show a toast
        Toast.makeText(this, "Loading attendance for selected date...", Toast.LENGTH_SHORT).show();
    }

    private void updateAttendanceStats() {
        if (studentList == null) return;

        int totalStudents = studentList.size();
        int presentCount = 0;
        int absentCount = 0;
        int lateCount = 0;

        for (Student student : studentList) {
            switch (student.getAttendanceStatus()) {
                case PRESENT:
                    presentCount++;
                    break;
                case ABSENT:
                    absentCount++;
                    break;
                case LATE:
                    lateCount++;
                    break;
            }
        }

        tvTotalStudents.setText(String.valueOf(totalStudents));
        tvPresentCount.setText(String.valueOf(presentCount + lateCount)); // Include late as present
        tvAbsentCount.setText(String.valueOf(absentCount));
    }

    private void markAllPresent() {
        for (Student student : studentList) {
            student.setAttendanceStatus(AttendanceStatus.PRESENT);
        }
        attendanceAdapter.notifyDataSetChanged();
        updateAttendanceStats();
        Toast.makeText(this, "All students marked present", Toast.LENGTH_SHORT).show();
    }

    private void saveAttendance() {
        // In a real app, you would save to database
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateStr = sdf.format(selectedCalendar.getTime());

        // Count attendance
        int presentCount = 0;
        int absentCount = 0;
        int lateCount = 0;

        for (Student student : studentList) {
            switch (student.getAttendanceStatus()) {
                case PRESENT:
                    presentCount++;
                    break;
                case ABSENT:
                    absentCount++;
                    break;
                case LATE:
                    lateCount++;
                    break;
            }
        }

        String message = String.format(
                "Attendance saved for %s\nPresent: %d, Late: %d, Absent: %d",
                dateStr, presentCount, lateCount, absentCount
        );

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

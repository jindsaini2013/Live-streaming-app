package com.example.live_streaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView tvGreeting, tvSelectedDate;
    private RecyclerView rvSchedule, rvStreaks;
    private View btnToday, btnTomorrow, btnPickDate;
    private ScheduleAdapter scheduleAdapter;
    private StreaksAdapter streaksAdapter;
    private Calendar selectedCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupRecyclerViews();
        setupDateButtons();
        loadTodaySchedule();

        return view;
    }

    private void initViews(View view) {

        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        rvSchedule = view.findViewById(R.id.rv_schedule);
        rvStreaks = view.findViewById(R.id.rv_streaks);
        btnToday = view.findViewById(R.id.btn_today);
        btnTomorrow = view.findViewById(R.id.btn_tomorrow);
        btnPickDate = view.findViewById(R.id.btn_pick_date);

        selectedCalendar = Calendar.getInstance();

        // Set greeting
    }

    private void setupRecyclerViews() {
        // Setup streaks
        List<StreakItem> streaks = getStreakItems();
        streaksAdapter = new StreaksAdapter(streaks);
        rvStreaks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvStreaks.setAdapter(streaksAdapter);

        // Setup schedule
        scheduleAdapter = new ScheduleAdapter(new ArrayList<>());
        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSchedule.setAdapter(scheduleAdapter);
    }

    private void setupDateButtons() {
        btnToday.setOnClickListener(v -> {
            selectedCalendar = Calendar.getInstance();
            updateSelectedDate();
            loadScheduleForDate();
        });

        btnTomorrow.setOnClickListener(v -> {
            selectedCalendar = Calendar.getInstance();
            selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
            updateSelectedDate();
            loadScheduleForDate();
        });

        btnPickDate.setOnClickListener(v -> {
            // Show date picker dialog
            showDatePickerDialog();
        });
    }

    private void updateSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        tvSelectedDate.setText(sdf.format(selectedCalendar.getTime()));
    }

    private void loadTodaySchedule() {
        updateSelectedDate();
        loadScheduleForDate();
    }

    private void loadScheduleForDate() {
        List<ScheduleItem> schedule = getScheduleForDate(selectedCalendar.getTime());
        scheduleAdapter.updateSchedule(schedule);
    }

    private List<StreakItem> getStreakItems() {
        List<StreakItem> streaks = new ArrayList<>();
        streaks.add(new StreakItem("Attendance Streak", "🔥 10 Days", "#FF9800"));
        streaks.add(new StreakItem("Test Streak", "📝 5 Tests", "#2196F3"));
        streaks.add(new StreakItem("Achievements", "🎉 3 Badges", "#9C27B0"));
        return streaks;
    }

    private List<ScheduleItem> getScheduleForDate(Date date) {
        List<ScheduleItem> schedule = new ArrayList<>();

        // Mock schedule data
        Calendar today = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTime(date);

        if (isSameDay(today, selectedDate)) {
            schedule.add(new ScheduleItem("Mathematics", "10:00 AM - 11:00 AM"));
            schedule.add(new ScheduleItem("Physics", "11:15 AM - 12:15 PM"));
            schedule.add(new ScheduleItem("Chemistry", "12:30 PM - 1:30 PM"));
        } else if (selectedDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) + 1) {
            schedule.add(new ScheduleItem("English", "9:00 AM - 10:00 AM"));
            schedule.add(new ScheduleItem("Biology", "11:00 AM - 12:00 PM"));
        } else {
            schedule.add(new ScheduleItem("History", "8:00 AM - 9:00 AM"));
            schedule.add(new ScheduleItem("Geography", "10:00 AM - 11:00 AM"));
        }

        return schedule;
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    private void showDatePickerDialog() {
        // Implement date picker dialog
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(year, month, dayOfMonth);
                    updateSelectedDate();
                    loadScheduleForDate();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}

package com.example.live_streaming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String RTMP_URL = "rtmp://10.125.21.240/live/stream_key";

    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private Chronometer chronometer;
    private File recordingFile;
    private boolean isRecording = false;

    private String currentSubject = "";
    private String currentChapter = "";
    private long recordingStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

        initializeViews();
        initializeVLC();
        setupClickListeners();
        showStreamingSetupDialog();
    }

    private void initializeViews() {
        surfaceView = findViewById(R.id.vlc_surface);
        chronometer = findViewById(R.id.chronometer);
    }
    private boolean isSurfaceReady = false;
    private int surfaceWidth, surfaceHeight;
    private void initializeVLC() {
        libVLC = VLCInstance.getInstance(this);
        mediaPlayer = new MediaPlayer(libVLC);
        mediaPlayer.getVLCVout().setVideoView(surfaceView);
        mediaPlayer.getVLCVout().attachViews();
        mediaPlayer.setAspectRatio(null);
        mediaPlayer.setScale(0);

        try {
            // Try different scaling values
            mediaPlayer.setScale(0.0f); // Auto scale

            // If that doesn't work, try manual scaling
            new android.os.Handler().postDelayed(() -> {
                try {
                    int surfaceWidth = surfaceView.getWidth();
                    int surfaceHeight = surfaceView.getHeight();
                    if (surfaceWidth > 0 && surfaceHeight > 0) {
                        mediaPlayer.getVLCVout().setWindowSize(surfaceWidth, surfaceHeight);
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Manual scaling failed", e);
                }
            }, 1000);

        } catch (Exception e) {
            Log.e("MainActivity", "Scaling failed", e);
        }
    }

    private void setupClickListeners() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btnStop = findViewById(R.id.btn_stop_recording);
        Button btnPause = findViewById(R.id.play_pause_button);

        btnBack.setOnClickListener(v -> finish());

        btnStop.setOnClickListener(v -> {
            if (isRecording) {
                new AlertDialog.Builder(this)
                        .setTitle("Stop Recording?")
                        .setMessage("This will stop the live stream and save the recording.")
                        .setPositiveButton("Stop & Save", (d, w) -> stopRecordingAndReturn())
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            else mediaPlayer.play();
        });
    }

    private void showStreamingSetupDialog() {
        String[] subjects = {"Mathematics", "Physics", "Chemistry", "Biology", "English", "History"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Setup Live Stream");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        TextView tvSubject = new TextView(this);
        tvSubject.setText("Select Subject:");
        layout.addView(tvSubject);

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        layout.addView(spinner);

        TextView tvChapter = new TextView(this);
        tvChapter.setText("Chapter/Topic:");
        tvChapter.setPadding(0, 20, 0, 0);
        layout.addView(tvChapter);

        EditText etChapter = new EditText(this);
        etChapter.setHint("Enter chapter or topic");
        layout.addView(etChapter);

        builder.setView(layout);
        builder.setPositiveButton("Start Streaming", (dialog, which) -> {
            currentSubject = subjects[spinner.getSelectedItemPosition()];
            currentChapter = etChapter.getText().toString().trim();
            if (currentChapter.isEmpty()) currentChapter = "General Topic";

            createRecordingFile();
            startRecording();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    private void createRecordingFile() {
        File recordingsDir = new File(getExternalFilesDir(null), "recordings");
        if (!recordingsDir.exists()) recordingsDir.mkdirs();

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        recordingFile = new File(recordingsDir, timestamp + ".ts");
    }

    private void startRecording() {
        isRecording = true;
        recordingStartTime = SystemClock.elapsedRealtime();
        chronometer.setBase(recordingStartTime);
        chronometer.start();

        Media media = new Media(libVLC, Uri.parse(RTMP_URL));

        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=3000");
        media.addOption(":live-caching=300");
        media.addOption(":audio-time-stretch");
        media.addOption(":drop-late-frames");
        media.addOption(":skip-frames");

        media.addOption(":sout=#duplicate{dst=display,dst=std{access=file,mux=ts,dst="
                + recordingFile.getAbsolutePath() + "}}");
        media.addOption(":no-sout-all");
        media.addOption(":sout-keep");

        mediaPlayer.setMedia(media);
        mediaPlayer.play();

        surfaceView.post(() -> {
            mediaPlayer.setAspectRatio(null);
            mediaPlayer.setScale(0);
            if (surfaceWidth > 0 && surfaceHeight > 0)
                mediaPlayer.getVLCVout().setWindowSize(surfaceWidth, surfaceHeight);
        });

        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Recording to: " + recordingFile.getAbsolutePath());
    }

    private void stopRecordingAndReturn() {
        if (!isRecording) return;
        isRecording = false;
        chronometer.stop();
        mediaPlayer.stop();

        saveRecordingToDatabase();

        Toast.makeText(this, "Recording saved successfully", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Recording saved to DB and file system");

        // Return to TeacherMainActivity
        Intent intent = new Intent(this, TeacherMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void saveRecordingToDatabase() {
        if (!recordingFile.exists()) {
            Log.e(TAG, "Recording file not found");
            return;
        }

        String durationStr = formatDuration(SystemClock.elapsedRealtime() - recordingStartTime);
        String dateCategory = getDateCategory();
        String teacherName = getTeacherName();
        String title = currentSubject + " - " + currentChapter;

        Recording recording = new Recording(
                title,
                currentSubject,
                currentChapter,
                durationStr,
                recordingFile.getAbsolutePath(),
                "",
                System.currentTimeMillis(),
                teacherName,
                dateCategory
        );

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            db.recordingDao().insertRecording(recording);
            Log.d(TAG, "Recording saved to database: " + title);
        }).start();
    }

    private String formatDuration(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private String getDateCategory() {
        return "today"; // Simplified - you can enhance this logic
    }

    private String getTeacherName() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return prefs.getString("username", "Teacher");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.release();
        }
    }
}

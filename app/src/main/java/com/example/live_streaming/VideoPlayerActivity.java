package com.example.live_streaming;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity {
    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        String videoPath = getIntent().getStringExtra("video_path");
        if (videoPath == null || !new File(videoPath).exists()) {
            Toast.makeText(this, "Video not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentMainActivity.class);
            intent.putExtra("open_tab", "recordings");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        surfaceView = findViewById(R.id.vlc_surface);
        initializeVLC();
        playVideo(videoPath);
    }

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
                    Log.e("VideoPlayerActivity", "Manual scaling failed", e);
                }
            }, 1000);

        } catch (Exception e) {
            Log.e("VideoPlayerActivity", "Scaling failed", e);
        }
    }

    private void playVideo(String videoPath) {
        Media media = new Media(libVLC, Uri.parse("file://" + videoPath));
        mediaPlayer.setMedia(media);
        mediaPlayer.play();
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

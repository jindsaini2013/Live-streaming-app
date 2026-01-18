package com.example.live_streaming;

import android.content.Context;
import android.util.Log;

import org.videolan.libvlc.LibVLC;

import java.util.ArrayList;

public class VLCInstance {

    private static LibVLC libVLC;

    public static synchronized LibVLC getInstance(Context context) {
        if (libVLC == null) {
            try {
                ArrayList<String> options = new ArrayList<>();
                options.add("--network-caching=3000");
                options.add("--aout=opensles"); // Enable audio
                options.add("--audio-time-stretch");

                Log.d("VLCInstance", "Creating LibVLC with audio support");
                libVLC = new LibVLC(context.getApplicationContext(), options);
                Log.d("VLCInstance", "LibVLC created successfully");

            } catch (Exception e) {
                Log.e("VLCInstance", "Failed with audio options, trying minimal", e);

                try {
                    ArrayList<String> basicOptions = new ArrayList<>();
                    basicOptions.add("--network-caching=3000");
                    libVLC = new LibVLC(context.getApplicationContext(), basicOptions);
                    Log.d("VLCInstance", "LibVLC created with basic options");

                } catch (Exception e2) {
                    Log.e("VLCInstance", "Failed to create LibVLC completely", e2);
                    libVLC = null;
                }
            }
        }
        return libVLC;
    }

    public static synchronized void releaseInstance() {
        if (libVLC != null) {
            try {
                libVLC.release();
                Log.d("VLCInstance", "LibVLC released successfully");
            } catch (Exception e) {
                Log.e("VLCInstance", "Error releasing LibVLC", e);
            } finally {
                libVLC = null;
            }
        }
    }
}

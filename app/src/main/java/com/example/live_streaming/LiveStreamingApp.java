package com.example.live_streaming;

import android.app.Application;
import android.util.Log;

public class LiveStreamingApp extends Application {
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("App", "Releasing LibVLC instance");
        VLCInstance.releaseInstance();
        System.runFinalization();
        System.gc();
    }
}

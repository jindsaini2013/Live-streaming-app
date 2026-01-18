package com.example.live_streaming;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordingDao {
    @Query("SELECT * FROM recording WHERE date = 'today' ORDER BY timestamp DESC")
    List<Recording> getTodayRecordings();

    @Query("SELECT * FROM recording WHERE date = 'yesterday' ORDER BY timestamp DESC")
    List<Recording> getYesterdayRecordings();

    @Query("SELECT * FROM recording WHERE date != 'today' AND date != 'yesterday' ORDER BY timestamp DESC")
    List<Recording> getOtherRecordings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecording(Recording recording);

    @Query("SELECT * FROM recording ORDER BY timestamp DESC")
    List<Recording> getAllRecordings();
}

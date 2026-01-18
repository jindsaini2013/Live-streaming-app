package com.example.live_streaming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.ViewHolder> {
    private List<RecordingItem> recordings;
    private OnRecordingClickListener clickListener;

    public interface OnRecordingClickListener {
        void onRecordingClick(RecordingItem recording);
    }

    public RecordingsAdapter(List<RecordingItem> recordings, OnRecordingClickListener listener) {
        this.recordings = recordings;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordingItem recording = recordings.get(position);
        holder.titleTextView.setText(recording.getTitle());
        holder.chapterTextView.setText(recording.getChapter());
        holder.durationTextView.setText("Duration: " + recording.getDuration());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onRecordingClick(recording);
        });
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, chapterTextView, durationTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_recording_title);
            chapterTextView = itemView.findViewById(R.id.tv_recording_chapter);
            durationTextView = itemView.findViewById(R.id.tv_recording_duration);
        }
    }
}

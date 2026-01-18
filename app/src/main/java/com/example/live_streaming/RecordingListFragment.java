package com.example.live_streaming;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecordingListFragment extends Fragment {
    private static final String ARG_TAB_NAME = "tab_name";
    private String tabName;
    private RecyclerView recyclerView;
    private RecordingsAdapter adapter;

    public static RecordingListFragment newInstance(String tabName) {
        RecordingListFragment fragment = new RecordingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAB_NAME, tabName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments() != null ? getArguments().getString(ARG_TAB_NAME) : "Other";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording_list, container, false);
        recyclerView = view.findViewById(R.id.rv_recordings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadRecordings();
        return view;
    }

    private void loadRecordings() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            List<Recording> dbRecordings;

            switch (tabName.toLowerCase()) {
                case "today":
                    dbRecordings = db.recordingDao().getTodayRecordings();
                    break;
                case "yesterday":
                    dbRecordings = db.recordingDao().getYesterdayRecordings();
                    break;
                default:
                    dbRecordings = db.recordingDao().getOtherRecordings();
                    break;
            }

            List<RecordingItem> items = new ArrayList<>();
            for (Recording r : dbRecordings) {
                items.add(new RecordingItem(
                        r.getTitle(),
                        r.getChapter(),
                        r.getDuration(),
                        r.getProgress(),
                        r.getRecordingPath(),
                        r.getDate()
                ));
            }

            requireActivity().runOnUiThread(() -> {
                adapter = new RecordingsAdapter(items, recording -> {
                    Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                    intent.putExtra("video_path", recording.getVideoPath());
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}

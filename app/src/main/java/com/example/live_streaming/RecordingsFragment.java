package com.example.live_streaming;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordingsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecordingsAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordings, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.view_pager_recordings);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_recordings);

        RecordingsPagerAdapter adapter = new RecordingsPagerAdapter(this, Arrays.asList("Today", "Yesterday", "Other"));
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(adapter.getTabs().get(position))
        ).attach();

        return view;
    }


}

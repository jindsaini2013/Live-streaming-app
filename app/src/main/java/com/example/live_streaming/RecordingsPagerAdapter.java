package com.example.live_streaming;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class RecordingsPagerAdapter extends FragmentStateAdapter {

    private List<String> tabs;

    public RecordingsPagerAdapter(@NonNull Fragment fragment, List<String> tabs) {
        super(fragment);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return RecordingListFragment.newInstance(tabs.get(position));
    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }

    public List<String> getTabs() {
        return tabs;
    }
}

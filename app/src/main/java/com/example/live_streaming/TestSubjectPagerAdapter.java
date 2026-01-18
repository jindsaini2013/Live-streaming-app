package com.example.live_streaming;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TestSubjectPagerAdapter extends FragmentStateAdapter {

    private List<String> subjects;

    public TestSubjectPagerAdapter(@NonNull Fragment fragment, List<String> subjects) {
        super(fragment);
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return TestSubjectFragment.newInstance(subjects.get(position));
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public List<String> getSubjects() {
        return subjects;
    }
}

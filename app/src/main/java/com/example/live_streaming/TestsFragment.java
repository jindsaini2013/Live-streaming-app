package com.example.live_streaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private static final String ARG_SUBJECT = "subject";
    private String subject;
    private TestSubjectPagerAdapter pagerAdapter;
    private AppDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);
        db = AppDatabase.getDatabase(requireContext().getApplicationContext());
        initViews(view);
        setupViewPager();
        if (getArguments() != null) {
            subject = getArguments().getString(ARG_SUBJECT);
        }

        return view;
    }
    public static TestsFragment newInstance(String subject) {
        TestsFragment fragment = new TestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT, subject);
        fragment.setArguments(args);
        return fragment;
    }
    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
    }

    private void setupViewPager() {
        new Thread(() -> {
            List<String> subjects = db.quizDao().getAllSubjects();
            requireActivity().runOnUiThread(() -> {
                pagerAdapter = new TestSubjectPagerAdapter(this, subjects);
                viewPager.setAdapter(pagerAdapter);
                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    tab.setText(pagerAdapter.getSubjects().get(position));
                }).attach();
            });
        }).start();
    }
}

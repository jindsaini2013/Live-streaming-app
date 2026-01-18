package com.example.live_streaming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestSubjectFragment extends Fragment {

    private static final String ARG_SUBJECT = "subject";
    private String subject;
    private RecyclerView recyclerView;
    private TestListAdapter adapter;
    private AppDatabase db;

    public static TestSubjectFragment newInstance(String subject) {
        TestSubjectFragment fragment = new TestSubjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT, subject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subject = getArguments().getString(ARG_SUBJECT);
        }
        db = AppDatabase.getDatabase(requireContext().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_subject, container, false);
        recyclerView = view.findViewById(R.id.rv_tests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TestListAdapter(new ArrayList<>(), test -> {
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra("test_name", test.getName());
            intent.putExtra("subject", subject);
            intent.putExtra("questions",test.getQuestionCount());
            intent.putExtra("complete",test.isCompleted());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadTests();

        return view;
    }

    /*private void setupRecyclerView() {
        List<TestItem> tests = getTestsForSubject(subject);
        adapter = new TestListAdapter(tests, test -> {
            // Navigate to quiz activity
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra("test_name", test.getName());
            intent.putExtra("subject", subject);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private List<TestItem> getTestsForSubject(String subject) {
        List<TestItem> tests = db.quizDao().getDistinctTestsForSubject(subject);
/*
        switch (subject) {
            case "Math":
                tests.add(new TestItem("Algebra Test", "5 Questions", "15 min", false));
                tests.add(new TestItem("Geometry Test", "5 Questions", "12 min", true));
                tests.add(new TestItem("Calculus Test", "8 Questions", "20 min", false));
                break;
            case "Science":
                tests.add(new TestItem("Physics Test", "5 Questions", "15 min", true));
                tests.add(new TestItem("Biology Test", "5 Questions", "12 min", false));
                tests.add(new TestItem("Chemistry Test", "6 Questions", "18 min", false));
                break;
            case "English":
                tests.add(new TestItem("Grammar Test", "10 Questions", "15 min", true));
                tests.add(new TestItem("Literature Test", "8 Questions", "20 min", false));
                break;
            case "History":
                tests.add(new TestItem("Ancient History", "7 Questions", "15 min", false));
                tests.add(new TestItem("Modern History", "6 Questions", "12 min", true));
                break;
        }

        if (getArguments() != null) {
            subject = getArguments().getString(ARG_SUBJECT);
        }
        db = AppDatabase.getDatabase(requireContext().getApplicationContext());

        return tests;
    } */
    private void loadTests() {
        new Thread(() -> {
            List<TestItem> tests = db.quizDao().getDistinctTestsForSubject(subject);
            Log.d("TestLoad", "Loaded " + tests.size() + " tests for subject: " + subject);
            requireActivity().runOnUiThread(() -> adapter.updateTestList(tests));
        }).start();
    }
}

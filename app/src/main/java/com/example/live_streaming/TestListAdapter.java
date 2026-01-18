package com.example.live_streaming;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {

    private List<TestItem> tests;
    private OnTestClickListener listener;

    public interface OnTestClickListener {
        void onTestClick(TestItem test);
    }

    public TestListAdapter(List<TestItem> tests, OnTestClickListener listener) {
        this.tests = tests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        TestItem test = tests.get(position);
        Log.d("AdapterBinding", "Binding test name: " + test.getName());
        holder.bind(test, listener);
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }
    public void updateTestList(List<TestItem> newTests) {
        tests.clear();
        tests.addAll(newTests);
        notifyDataSetChanged();
    }
    public static class TestViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvTestName;
        private TextView tvQuestionCount;
        private TextView tvDuration;
        private ImageView ivStatus;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_test);
            tvTestName = itemView.findViewById(R.id.tv_test_name);
            tvQuestionCount = itemView.findViewById(R.id.tv_question_count);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            ivStatus = itemView.findViewById(R.id.iv_status);
        }

        public void bind(TestItem test, OnTestClickListener listener) {
            tvTestName.setText(test.getName());
            tvQuestionCount.setText(test.getQuestionCount());
            tvDuration.setText(test.getDuration());

            if (test.isCompleted()) {
                ivStatus.setImageResource(R.drawable.ic_check_circle);
                ivStatus.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.status_success));
            } else {
                ivStatus.setImageResource(R.drawable.ic_play_circle);
                ivStatus.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.primary_blue));
            }

            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTestClick(test);
                }
            });
        }

    }
}

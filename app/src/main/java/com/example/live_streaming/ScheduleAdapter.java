package com.example.live_streaming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ScheduleItem> scheduleItems;

    public ScheduleAdapter(List<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    public void updateSchedule(List<ScheduleItem> newSchedule) {
        this.scheduleItems = newSchedule;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleItem item = scheduleItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSubject;
        private TextView tvTime;
        private ImageView ivSubjectIcon;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivSubjectIcon = itemView.findViewById(R.id.iv_subject_icon);
        }

        public void bind(ScheduleItem item) {
            tvSubject.setText(item.getSubject());
            tvTime.setText(item.getTime());

            // Set appropriate icon based on subject
            setSubjectIcon(item.getSubject());
        }

        private void setSubjectIcon(String subject) {
            int iconRes = R.drawable.ic_book; // default icon

            switch (subject.toLowerCase()) {
                case "mathematics":
                case "math":
                    iconRes = R.drawable.ic_school;
                    break;
                case "physics":
                    iconRes = R.drawable.ic_school;
                    break;
                case "chemistry":
                    iconRes = R.drawable.ic_school;
                    break;
                case "english":
                    iconRes = R.drawable.ic_school;
                    break;
                case "biology":
                    iconRes = R.drawable.ic_school;
                    break;
                case "history":
                    iconRes = R.drawable.ic_school;
                    break;
                case "geography":
                    iconRes = R.drawable.ic_school;
                    break;
                default:
                    iconRes = R.drawable.ic_book;
                    break;
            }

            ivSubjectIcon.setImageResource(iconRes);
        }
    }
}

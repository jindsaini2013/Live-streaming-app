package com.example.live_streaming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<Student> students;
    private OnAttendanceChangeListener listener;

    public interface OnAttendanceChangeListener {
        void onAttendanceChanged();
    }

    public AttendanceAdapter(List<Student> students, OnAttendanceChangeListener listener) {
        this.students = students;
        this.listener = listener;
    }

    public void updateStudents(List<Student> newStudents) {
        this.students = newStudents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student, position, listener);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvStudentName;
        private TextView tvStudentEmail;
        private ImageView ivStudentAvatar;
        private MaterialButton btnPresent;
        private MaterialButton btnAbsent;
        private MaterialButton btnLate;
        private MaterialButtonToggleGroup toggleGroup;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_student);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvStudentEmail = itemView.findViewById(R.id.tv_student_email);
            ivStudentAvatar = itemView.findViewById(R.id.iv_student_avatar);
            btnPresent = itemView.findViewById(R.id.btn_present);
            btnAbsent = itemView.findViewById(R.id.btn_absent);
            btnLate = itemView.findViewById(R.id.btn_late);
            toggleGroup = itemView.findViewById(R.id.toggle_group);
        }

        public void bind(Student student, int position, OnAttendanceChangeListener listener) {
            tvStudentName.setText(student.getName());
            tvStudentEmail.setText(student.getEmail());

            // Set avatar with first letter of name
            String firstLetter = student.getName().substring(0, 1).toUpperCase();
            setAvatarBackground(student.getName().hashCode());

            // CRITICAL FIX: Clear all previous listeners
            toggleGroup.clearOnButtonCheckedListeners();

            // Clear previous selections
            toggleGroup.clearChecked();

            // Set current attendance status
            switch (student.getAttendanceStatus()) {
                case PRESENT:
                    toggleGroup.check(R.id.btn_present);
                    break;
                case ABSENT:
                    toggleGroup.check(R.id.btn_absent);
                    break;
                case LATE:
                    toggleGroup.check(R.id.btn_late);
                    break;
            }

            // Set up NEW button listener using getAdapterPosition
            toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (isChecked) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        Student currentStudent = students.get(adapterPosition);

                        AttendanceStatus newStatus;
                        if (checkedId == R.id.btn_present) {
                            newStatus = AttendanceStatus.PRESENT;
                        } else if (checkedId == R.id.btn_absent) {
                            newStatus = AttendanceStatus.ABSENT;
                        } else {
                            newStatus = AttendanceStatus.LATE;
                        }

                        currentStudent.setAttendanceStatus(newStatus);
                        if (listener != null) {
                            listener.onAttendanceChanged();
                        }
                    }
                }
            });
        }

        private void setAvatarBackground(int hashCode) {
            int[] colors = {
                    R.color.streak_attendance_dark,
                    R.color.streak_test_dark,
                    R.color.streak_achievement_dark,
                    R.color.primary_blue,
                    R.color.secondary_purple
            };

            int colorIndex = Math.abs(hashCode) % colors.length;
            int color = ContextCompat.getColor(itemView.getContext(), colors[colorIndex]);
            ivStudentAvatar.setBackgroundColor(color);
        }
    }
}

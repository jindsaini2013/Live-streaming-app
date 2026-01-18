package com.example.live_streaming;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StreaksAdapter extends RecyclerView.Adapter<StreaksAdapter.StreakViewHolder> {

    private List<StreakItem> streaks;

    public StreaksAdapter(List<StreakItem> streaks) {
        this.streaks = streaks;
    }

    @NonNull
    @Override
    public StreakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_streak_card, parent, false);
        return new StreakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreakViewHolder holder, int position) {
        StreakItem streak = streaks.get(position);
        holder.bind(streak);
    }

    @Override
    public int getItemCount() {
        return streaks.size();
    }

    public static class StreakViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvStreakValue;
        private TextView tvStreakTitle;

        public StreakViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_streak);
            tvStreakValue = itemView.findViewById(R.id.tv_streak_value);
            tvStreakTitle = itemView.findViewById(R.id.tv_streak_title);
        }

        public void bind(StreakItem streak) {
            tvStreakValue.setText(streak.getValue());
            tvStreakTitle.setText(streak.getTitle());

            // Create gradient background
            GradientDrawable gradient = new GradientDrawable();
            gradient.setOrientation(GradientDrawable.Orientation.TL_BR);

            int baseColor = Color.parseColor(streak.getColor());
            int lightColor = Color.parseColor(streak.getColor());
            // Make lighter version
            int alpha = 150;
            lightColor = Color.argb(alpha, Color.red(lightColor), Color.green(lightColor), Color.blue(lightColor));

            gradient.setColors(new int[]{lightColor, baseColor});
            gradient.setCornerRadius(24f);

            cardView.setBackground(gradient);
        }
    }
}

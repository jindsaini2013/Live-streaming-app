package com.example.live_streaming;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout messageContainer;
        private TextView tvMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageContainer = itemView.findViewById(R.id.message_container);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }

        public void bind(ChatMessage message) {
            tvMessage.setText(message.getMessage());

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageContainer.getLayoutParams();

            if (message.isUser()) {
                // User message - align right, blue background
                params.gravity = Gravity.END;
                messageContainer.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_user_message));
                tvMessage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.appbar_text));
            } else {
                // Bot message - align left, light background
                params.gravity = Gravity.START;
                messageContainer.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_bot_message));
                tvMessage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_primary));
            }

            messageContainer.setLayoutParams(params);
        }
    }
}

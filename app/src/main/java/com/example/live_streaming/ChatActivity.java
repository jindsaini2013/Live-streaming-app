package com.example.live_streaming;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ProgressBar progressBar;
    private LinearLayout feedbackLayout;
    private Button btnYes, btnNo;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages;
    private OkHttpClient httpClient;

    private boolean isLoading = false;
    private int feedbackAttempts = 0;
    private String currentQuery = "";

    // REPLACE WITH YOUR ACTUAL GEMINI API KEY
    private static final String GEMINI_API_KEY = "AIzaSyD4NkpMT2LUBgDaDzcsScc_b5-tkpkSmnY"; // Your API key here
    // Update this constant at the top of your class:
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent" ;
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity fullscreen and extend under status bar
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_chat);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupHttpClient();
        setupButtons();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            addBotMessage("Hello! I'm your AI study assistant. How can I help you today?");
        }, 100);
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("AI Study Assistant");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(8f);

            // Make toolbar extend under status bar
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set status bar color to match toolbar
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary_blue));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_chat);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        progressBar = findViewById(R.id.progress_bar);
        feedbackLayout = findViewById(R.id.feedback_layout);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
    }

    private void setupRecyclerView() {
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
    }

    private void setupHttpClient() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private void setupButtons() {
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty() && !isLoading) {
                handleSubmitted(message);
            }
        });

        btnYes.setOnClickListener(v -> {
            hideFeedback();
            feedbackAttempts = 0;
            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
        });

        btnNo.setOnClickListener(v -> {
            hideFeedback();
            if (feedbackAttempts < 2) {
                feedbackAttempts++;
                sendMessage(currentQuery + " (Please provide a more detailed explanation)");
            } else {
                addBotMessage("I apologize for not meeting your expectations. A teacher will contact you soon.");
                feedbackAttempts = 0;
            }
        });
    }

    private void handleSubmitted(String text) {
        etMessage.setText("");
        addUserMessage(text);
        sendMessage(text);
    }

    private void addUserMessage(String message) {
        messages.add(new ChatMessage(message, true));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        messages.add(new ChatMessage(message, false));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void sendMessage(String text) {
        if (isLoading) return;

        setLoading(true);
        currentQuery = text;

        callGeminiAPI(text);
    }

    private void callGeminiAPI(String query) {
        try {
            String educationalPrompt = "You are a helpful educational assistant for students. " +
                    "Please provide clear, encouraging, and educational responses. " +
                    "If asked about homework, guide them to learn rather than giving direct answers. " +
                    "Student's question: " + query;

            // Create the request body according to Gemini API format
            JSONObject requestBody = new JSONObject();

            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();

            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            part.put("text", educationalPrompt);
            parts.put(part);

            content.put("parts", parts);
            contents.put(content);
            requestBody.put("contents", contents);

            Log.d(TAG, "Request URL: " + GEMINI_API_URL + "?key=" + GEMINI_API_KEY);
            Log.d(TAG, "Request body: " + requestBody.toString());

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + "?key=" + GEMINI_API_KEY)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "API call failed", e);
                    runOnUiThread(() -> {
                        setLoading(false);
                        addBotMessage("Sorry, I'm having trouble connecting. Please check your internet connection and try again.");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response status: " + response.code());
                    Log.d(TAG, "Response body: " + responseBody);

                    runOnUiThread(() -> {
                        setLoading(false);

                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);

                                if (jsonResponse.has("candidates")) {
                                    JSONArray candidates = jsonResponse.getJSONArray("candidates");

                                    if (candidates.length() > 0) {
                                        JSONObject candidate = candidates.getJSONObject(0);
                                        JSONObject content = candidate.getJSONObject("content");
                                        JSONArray parts = content.getJSONArray("parts");

                                        if (parts.length() > 0) {
                                            String botResponse = parts.getJSONObject(0).getString("text");
                                            addBotMessage(botResponse);
                                            showFeedback();
                                        } else {
                                            addBotMessage("I couldn't generate a response. Please try rephrasing your question.");
                                        }
                                    } else {
                                        addBotMessage("No response generated. Please try again.");
                                    }
                                } else {
                                    addBotMessage("Unexpected response format. Please try again.");
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "JSON parsing error", e);
                                addBotMessage("Sorry, I received an unexpected response.");
                            }
                        } else {
                            // Handle different error codes
                            if (response.code() == 404) {
                                addBotMessage("API endpoint not found. Please check the configuration.");
                            } else if (response.code() == 401 || response.code() == 403) {
                                addBotMessage("Authentication failed. Please check your API key.");
                            }
                            else {
                                addBotMessage("Error " + response.code() + ": " + responseBody);
                            }
                        }
                    });
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "JSON creation error", e);
            setLoading(false);
            addBotMessage("Sorry, there was an error processing your request.");
        }
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(!loading);
        etMessage.setEnabled(!loading);
    }

    private void showFeedback() {
        feedbackLayout.setVisibility(View.VISIBLE);
    }

    private void hideFeedback() {
        feedbackLayout.setVisibility(View.GONE);
    }

    private void scrollToBottom() {
        if (messages.size() > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(messages.size() - 1);
                }
            }, 100);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
        }
    }
}

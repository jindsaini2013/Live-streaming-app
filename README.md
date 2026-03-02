# EduStream 🚀
### Live Classes from Cities to Rural Villages

### 📱 What is EduStream?
EduStream bridges the urban-rural education divide by delivering live classes from expert urban teachers directly to students in remote villages via simple smartphones. It creates a complete digital classroom with real-time streaming, AI chatbot support, adaptive quizzes, offline lectures, smart attendance, and an Opportunities Hub for scholarships and career guidance.


### **✨ Key Features**

| Feature | Description |
|---------|-------------|
| **🎥 Live Streaming** | Real-time classes from urban teachers to rural students |
| **📚 Offline Lectures** | Auto-recorded sessions for anytime playback |
| **🤖 AI Chatbot** | Bilingual doubt clearing (Hindi+English) |
| **✅ Adaptive Quizzes** | Instant feedback + performance tracking |
| **📊 Smart Attendance** | Streak system for consistent participation |
| **🎓 Opportunities Hub** | Scholarships + career guidance |


### 🛠 Tech Stack
Frontend: Android Studio + Java

Backend: FastAPI (Python)

Database: Room Database (Offline-first)

Cloud: Firebase/Supabase

AI: Gemini API (NLP Chatbot)

Streaming: WebRTC

Architecture: MVVM + Clean Architecture

### 🚀 Quick Start
#### Prerequisites
- Android Studio Flamingo | 2022.2.1 or later

- Android SDK API 24+

- Java 11+

1. Clone the repository
```bash
git clone https://github.com/yourusername/edustream.git 
cd edustream
```
2. Open in Android Studio
```text
File → Open → Select project folder
```
3. Setup Firebase/Supabase (Optional)
```text
1. Create Firebase project
2. Add `google-services.json` to `app/`
3. Update API keys in `Constants.java`
```
4. Build & Run
```bash
# Sync Gradle
# Build → Make Project (Ctrl+F9)
# Run → Run 'app' (Shift+F10)
```

### **⚙️ Environment Setup**

| Dependency | Version | Purpose |
|------------|---------|---------|
| **Room Database** | `2.6.1` | Offline quiz storage |
| **ViewPager2** | `1.0.0` | Subject tabs |
| **RecyclerView** | `1.3.2` | Test listing |
| **Material Design** | `1.12.0` | Modern UI |
| **Firebase** | `32.7.0` | Cloud sync |

### 🤖 AI Chatbot Integration
```java
// Gemini API integration example
private void sendQueryToChatbot(String query) {
    ChatRequest request = new ChatRequest(query, "hindi_english");
    geminiApi.processQuery(request, new Callback<String>() {
        @Override
        public void onResponse(String response) {
            updateChatbotUI(response);
        }
    });
}
```

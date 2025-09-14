package com.smartappgatekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Real-time chat service
 * Manages live chat messages and social interactions
 */
public class RealtimeChatService extends Service {
    
    private static final String TAG = "RealtimeChatService";
    private static final int MESSAGE_INTERVAL = 20000; // 20 seconds
    
    private Handler handler;
    private Runnable messageRunnable;
    private boolean isRunning = false;
    private List<ChatMessage> messages = new ArrayList<>();
    
    public static class ChatMessage {
        public String id;
        public String username;
        public String message;
        public long timestamp;
        public String avatar;
        public boolean isFromCurrentUser;
        
        public ChatMessage(String id, String username, String message, long timestamp, String avatar, boolean isFromCurrentUser) {
            this.id = id;
            this.username = username;
            this.message = message;
            this.timestamp = timestamp;
            this.avatar = avatar;
            this.isFromCurrentUser = isFromCurrentUser;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        initializeChat();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRealtimeMessages();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private void initializeChat() {
        // Initialize with sample messages
        messages.add(new ChatMessage("1", "QuizMaster", "Great job on the quiz! 🎉", System.currentTimeMillis() - 300000, "👑", false));
        messages.add(new ChatMessage("2", "BrainBox", "Thanks! The questions were challenging", System.currentTimeMillis() - 240000, "🧠", false));
        messages.add(new ChatMessage("3", "You", "I agree, really enjoyed it!", System.currentTimeMillis() - 180000, "😊", true));
        messages.add(new ChatMessage("4", "SmartLearner", "Anyone up for a study group?", System.currentTimeMillis() - 120000, "🎓", false));
    }
    
    private void startRealtimeMessages() {
        if (isRunning) return;
        
        isRunning = true;
        messageRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    generateRealtimeMessage();
                    handler.postDelayed(this, MESSAGE_INTERVAL);
                }
            }
        };
        handler.post(messageRunnable);
    }
    
    private void generateRealtimeMessage() {
        Random random = new Random();
        String[] usernames = {"QuizMaster", "BrainBox", "SmartLearner", "KnowledgeSeeker", "StudyBuddy", "QuizChamp", "LearningPro", "BrainTrainer"};
        String[] avatars = {"👑", "🧠", "🎓", "🔍", "📚", "🏆", "💡", "🚀"};
        String[] messageTemplates = {
            "Just completed the advanced quiz! 💪",
            "Anyone else struggling with algorithms?",
            "Great tips in the learning section!",
            "My streak is getting longer! 🔥",
            "The new themes look amazing! 🎨",
            "Who's up for a challenge?",
            "This app is really helping me learn!",
            "Just earned 100 coins! 🪙",
            "The leaderboard is getting competitive!",
            "Love the real-time updates! ⚡"
        };
        
        String username = usernames[random.nextInt(usernames.length)];
        String avatar = avatars[random.nextInt(avatars.length)];
        String message = messageTemplates[random.nextInt(messageTemplates.length)];
        
        ChatMessage chatMessage = new ChatMessage(
            String.valueOf(System.currentTimeMillis()),
            username,
            message,
            System.currentTimeMillis(),
            avatar,
            false
        );
        
        messages.add(chatMessage);
        
        // Keep only last 50 messages
        if (messages.size() > 50) {
            messages.remove(0);
        }
        
        // Broadcast new message
        Intent intent = new Intent("CHAT_MESSAGE_RECEIVED");
        intent.putExtra("message_id", chatMessage.id);
        intent.putExtra("username", chatMessage.username);
        intent.putExtra("message", chatMessage.message);
        intent.putExtra("avatar", chatMessage.avatar);
        sendBroadcast(intent);
        
        Log.d(TAG, "Generated new chat message from " + username);
    }
    
    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }
    
    public void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(
            String.valueOf(System.currentTimeMillis()),
            "You",
            message,
            System.currentTimeMillis(),
            "😊",
            true
        );
        
        messages.add(chatMessage);
        
        // Broadcast sent message
        Intent intent = new Intent("CHAT_MESSAGE_SENT");
        intent.putExtra("message_id", chatMessage.id);
        intent.putExtra("message", chatMessage.message);
        sendBroadcast(intent);
        
        Log.d(TAG, "Sent message: " + message);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (messageRunnable != null) {
            handler.removeCallbacks(messageRunnable);
        }
    }
}

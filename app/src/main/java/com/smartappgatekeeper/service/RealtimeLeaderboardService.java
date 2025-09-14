package com.smartappgatekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Real-time leaderboard service
 * Manages live leaderboard updates and rankings
 */
public class RealtimeLeaderboardService extends Service {
    
    private static final String TAG = "RealtimeLeaderboardService";
    private static final int UPDATE_INTERVAL = 15000; // 15 seconds
    
    private Handler handler;
    private Runnable updateRunnable;
    private boolean isRunning = false;
    private List<LeaderboardEntry> leaderboard = new ArrayList<>();
    
    public static class LeaderboardEntry {
        public String username;
        public int score;
        public int rank;
        public String avatar;
        public boolean isOnline;
        
        public LeaderboardEntry(String username, int score, int rank, String avatar, boolean isOnline) {
            this.username = username;
            this.score = score;
            this.rank = rank;
            this.avatar = avatar;
            this.isOnline = isOnline;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        initializeLeaderboard();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRealtimeUpdates();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private void initializeLeaderboard() {
        // Initialize with sample data
        leaderboard.add(new LeaderboardEntry("QuizMaster", 2500, 1, "👑", true));
        leaderboard.add(new LeaderboardEntry("BrainBox", 2300, 2, "🧠", true));
        leaderboard.add(new LeaderboardEntry("SmartLearner", 2100, 3, "🎓", false));
        leaderboard.add(new LeaderboardEntry("KnowledgeSeeker", 1900, 4, "🔍", true));
        leaderboard.add(new LeaderboardEntry("StudyBuddy", 1700, 5, "📚", false));
        leaderboard.add(new LeaderboardEntry("QuizChamp", 1500, 6, "🏆", true));
        leaderboard.add(new LeaderboardEntry("LearningPro", 1300, 7, "💡", false));
        leaderboard.add(new LeaderboardEntry("BrainTrainer", 1100, 8, "🚀", true));
    }
    
    private void startRealtimeUpdates() {
        if (isRunning) return;
        
        isRunning = true;
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    updateLeaderboard();
                    handler.postDelayed(this, UPDATE_INTERVAL);
                }
            }
        };
        handler.post(updateRunnable);
    }
    
    private void updateLeaderboard() {
        Random random = new Random();
        
        // Simulate score updates
        for (LeaderboardEntry entry : leaderboard) {
            if (random.nextDouble() < 0.3) { // 30% chance of score update
                entry.score += random.nextInt(50) + 10;
                Log.d(TAG, "Updated score for " + entry.username + ": " + entry.score);
            }
            
            // Simulate online status changes
            if (random.nextDouble() < 0.2) { // 20% chance of status change
                entry.isOnline = !entry.isOnline;
                Log.d(TAG, entry.username + " is now " + (entry.isOnline ? "online" : "offline"));
            }
        }
        
        // Sort by score and update ranks
        Collections.sort(leaderboard, (a, b) -> Integer.compare(b.score, a.score));
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).rank = i + 1;
        }
        
        // Broadcast leaderboard update
        Intent intent = new Intent("LEADERBOARD_UPDATED");
        intent.putExtra("leaderboard_data", "updated");
        sendBroadcast(intent);
        
        Log.d(TAG, "Leaderboard updated with " + leaderboard.size() + " entries");
    }
    
    public List<LeaderboardEntry> getLeaderboard() {
        return new ArrayList<>(leaderboard);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }
}

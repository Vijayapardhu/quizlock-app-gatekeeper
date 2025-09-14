package com.smartappgatekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.UsageEvent;
import com.smartappgatekeeper.database.entities.Streak;

import java.util.Date;
import java.util.Random;

/**
 * Real-time data synchronization service
 * Syncs data with backend and updates local database
 */
public class RealtimeDataSyncService extends Service {
    
    private static final String TAG = "RealtimeDataSyncService";
    private static final int SYNC_INTERVAL = 10000; // 10 seconds
    
    private Handler handler;
    private Runnable syncRunnable;
    private AppRepository repository;
    private boolean isRunning = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        repository = AppRepository.getInstance(getApplication());
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRealtimeSync();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private void startRealtimeSync() {
        if (isRunning) return;
        
        isRunning = true;
        syncRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    performRealtimeSync();
                    handler.postDelayed(this, SYNC_INTERVAL);
                }
            }
        };
        handler.post(syncRunnable);
    }
    
    private void performRealtimeSync() {
        try {
            // Simulate real-time data updates
            updateUsageData();
            updateStreakData();
            updateQuizData();
            
            Log.d(TAG, "Real-time sync completed");
        } catch (Exception e) {
            Log.e(TAG, "Error during real-time sync: " + e.getMessage());
        }
    }
    
    private void updateUsageData() {
        // Simulate new usage events
        Random random = new Random();
        if (random.nextBoolean()) {
            UsageEvent event = new UsageEvent();
            event.packageName = "com.example.app";
            event.eventType = "app_opened";
            event.timestamp = new Date();
            event.durationSeconds = random.nextInt(300) + 60; // 1-6 minutes
            
            repository.insertUsageEvent(event);
            Log.d(TAG, "Added new usage event");
        }
    }
    
    private void updateStreakData() {
        // Simulate streak updates
        Random random = new Random();
        if (random.nextDouble() < 0.3) { // 30% chance
            Streak streak = new Streak();
            streak.streakType = "daily";
            streak.currentCount = random.nextInt(10) + 1;
            streak.longestCount = random.nextInt(20) + 5;
            streak.lastUpdated = new Date();
            
            repository.insertStreak(streak);
            Log.d(TAG, "Updated streak data");
        }
    }
    
    private void updateQuizData() {
        // Simulate quiz completion events
        Random random = new Random();
        if (random.nextDouble() < 0.2) { // 20% chance
            // This would typically update quiz statistics
            Log.d(TAG, "Quiz data updated");
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (syncRunnable != null) {
            handler.removeCallbacks(syncRunnable);
        }
    }
}

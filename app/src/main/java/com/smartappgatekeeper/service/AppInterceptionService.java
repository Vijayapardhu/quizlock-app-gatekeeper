package com.smartappgatekeeper.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.app.NotificationCompat;
import android.view.accessibility.AccessibilityNodeInfo;

import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.TargetApp;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Accessibility Service for intercepting app launches
 * FR-001: System shall intercept all configured target apps
 * FR-002: System shall display quiz modal within 200ms of app launch attempt
 * FR-003: System shall prevent app access until quiz is completed successfully
 */
public class AppInterceptionService extends AccessibilityService {
    private static final String TAG = "AppInterceptionService";
    
    private AppRepository repository;
    private List<TargetApp> enabledTargetApps;
    
    @Override
    public void onCreate() {
        super.onCreate();
        repository = AppRepository.getInstance(getApplication());
        loadEnabledTargetApps();
        Log.d(TAG, "AppInterceptionService created");
    }
    
    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        
        // Configure accessibility service
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.notificationTimeout = 100;
        
        setServiceInfo(info);
        Log.d(TAG, "AppInterceptionService connected");
    }
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? 
                event.getPackageName().toString() : null;
            
            if (packageName != null && isTargetApp(packageName)) {
                Log.d(TAG, "Intercepted app launch: " + packageName);
                handleAppInterception(packageName);
            }
        }
    }
    
    /**
     * Show notification when daily limit is reached
     */
    private void showDailyLimitReachedNotification(String packageName) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                "daily_limit_channel", 
                "Daily Limit Notifications", 
                NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        
        Intent notificationIntent = new Intent(this, com.smartappgatekeeper.ui.activities.MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        
        Notification notification = new NotificationCompat.Builder(this, "daily_limit_channel")
            .setContentTitle("🚫 Daily Limit Reached")
            .setContentText("You've reached your daily limit for " + packageName + ". Try again tomorrow!")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build();
        
        notificationManager.notify(3, notification);
        
        // Also show a toast for immediate feedback
        android.widget.Toast.makeText(this, 
            "Daily limit reached for " + packageName, 
            android.widget.Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onInterrupt() {
        Log.d(TAG, "AppInterceptionService interrupted");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AppInterceptionService destroyed");
    }
    
    /**
     * Load enabled target apps from database
     */
    private void loadEnabledTargetApps() {
        repository.getEnabledTargetApps().observeForever(targetApps -> {
            enabledTargetApps = targetApps;
            Log.d(TAG, "Loaded " + (targetApps != null ? targetApps.size() : 0) + " enabled target apps");
        });
    }
    
    /**
     * Check if the given package name is a target app
     */
    private boolean isTargetApp(String packageName) {
        if (enabledTargetApps == null) {
            return false;
        }
        
        for (TargetApp targetApp : enabledTargetApps) {
            if (targetApp.packageName.equals(packageName) && targetApp.isEnabled) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Handle app interception by launching quiz
     */
    private void handleAppInterception(String packageName) {
        CompletableFuture<TargetApp> targetAppFuture = repository.getTargetAppByPackageAsync(packageName);
        
        targetAppFuture.thenAccept(targetApp -> {
            if (targetApp != null && targetApp.isEnabled) {
                // Check if app has reached daily limit
                if (targetApp.currentUsesToday >= targetApp.maxUsesPerDay) {
                    Log.d(TAG, "App " + packageName + " has reached daily limit");
                    // Show daily limit reached notification
                    showDailyLimitReachedNotification(packageName);
                    return;
                }
                
                // Launch quiz activity
                Intent quizIntent = new Intent(this, com.smartappgatekeeper.ui.activities.QuizActivity.class);
                quizIntent.putExtra("target_app", targetApp);
                quizIntent.putExtra("package_name", packageName);
                quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                startActivity(quizIntent);
                
                // Log the interception event
                logInterceptionEvent(packageName, targetApp.appName);
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Error handling app interception for " + packageName, throwable);
            return null;
        });
    }
    
    /**
     * Log app interception event
     */
    private void logInterceptionEvent(String packageName, String appName) {
        com.smartappgatekeeper.database.entities.UsageEvent event = 
            new com.smartappgatekeeper.database.entities.UsageEvent();
        event.packageName = packageName;
        event.appName = appName;
        event.eventType = "app_launch";
        event.timestamp = new java.util.Date();
        event.success = false; // Will be updated when quiz is completed
        
        repository.insertUsageEvent(event);
    }
}
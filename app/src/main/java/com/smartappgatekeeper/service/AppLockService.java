package com.smartappgatekeeper.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.activities.QuizActivity;

/**
 * Service for blocking apps and showing quiz unlock interface
 * This service creates a system overlay that blocks access to target apps
 * Users must complete a quiz to unlock the app
 */
public class AppLockService extends Service {
    private static final String TAG = "AppLockService";
    private static final String CHANNEL_ID = "AppLockServiceChannel";
    private static final int NOTIFICATION_ID = 2;
    
    private WindowManager windowManager;
    private View lockOverlay;
    private String targetPackage;
    private String targetAppName;
    private boolean isLocked = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Log.d(TAG, "AppLockService created");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            targetPackage = intent.getStringExtra("target_package");
            targetAppName = intent.getStringExtra("target_app_name");
            
            if (targetPackage != null) {
                startForeground(NOTIFICATION_ID, createNotification());
                showLockOverlay();
                isLocked = true;
                Log.d(TAG, "App locked: " + targetAppName);
            }
        }
        
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLockOverlay();
        Log.d(TAG, "AppLockService destroyed");
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Show the lock overlay that blocks access to the target app
     */
    private void showLockOverlay() {
        if (lockOverlay != null) {
            return; // Already showing
        }
        
        try {
            // Create the lock overlay view
            lockOverlay = createLockOverlayView();
            
            // Set up window parameters for system overlay
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O 
                    ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            );
            
            params.gravity = Gravity.TOP | Gravity.LEFT;
            
            // Add the overlay to the window manager
            windowManager.addView(lockOverlay, params);
            
            Log.d(TAG, "Lock overlay displayed for: " + targetAppName);
            
        } catch (Exception e) {
            Log.e(TAG, "Error showing lock overlay", e);
        }
    }
    
    /**
     * Create the lock overlay view with quiz unlock interface
     */
    private View createLockOverlayView() {
        // Inflate the lock screen layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View overlayView = inflater.inflate(R.layout.layout_app_lock_screen, null);
        
        // Set up the UI elements
        TextView titleText = overlayView.findViewById(R.id.text_lock_title);
        TextView messageText = overlayView.findViewById(R.id.text_lock_message);
        Button quizButton = overlayView.findViewById(R.id.button_take_quiz);
        Button settingsButton = overlayView.findViewById(R.id.button_settings);
        
        if (titleText != null) {
            titleText.setText("🔒 App Locked");
        }
        
        if (messageText != null) {
            messageText.setText("Time's up! " + targetAppName + " is now locked.\n\nComplete a quiz to unlock the app and continue using it.");
        }
        
        if (quizButton != null) {
            quizButton.setOnClickListener(v -> {
                startQuizToUnlock();
            });
        }
        
        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> {
                openSettings();
            });
        }
        
        return overlayView;
    }
    
    /**
     * Start quiz activity to unlock the app
     */
    private void startQuizToUnlock() {
        try {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            quizIntent.putExtra("unlock_mode", true);
            quizIntent.putExtra("target_package", targetPackage);
            quizIntent.putExtra("target_app_name", targetAppName);
            startActivity(quizIntent);
            
            Log.d(TAG, "Quiz started for unlock: " + targetAppName);
            
        } catch (Exception e) {
            Log.e(TAG, "Error starting quiz", e);
        }
    }
    
    /**
     * Open app settings
     */
    private void openSettings() {
        try {
            Intent settingsIntent = new Intent(this, com.smartappgatekeeper.ui.activities.MainActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            settingsIntent.putExtra("open_settings", true);
            startActivity(settingsIntent);
            
            Log.d(TAG, "Settings opened");
            
        } catch (Exception e) {
            Log.e(TAG, "Error opening settings", e);
        }
    }
    
    /**
     * Remove the lock overlay
     */
    private void removeLockOverlay() {
        if (lockOverlay != null && windowManager != null) {
            try {
                windowManager.removeView(lockOverlay);
                lockOverlay = null;
                isLocked = false;
                Log.d(TAG, "Lock overlay removed");
            } catch (Exception e) {
                Log.e(TAG, "Error removing lock overlay", e);
            }
        }
    }
    
    /**
     * Unlock the app (called when quiz is completed successfully)
     */
    public void unlockApp() {
        removeLockOverlay();
        stopForeground(true);
        stopSelf();
        Log.d(TAG, "App unlocked: " + targetAppName);
    }
    
    /**
     * Create notification channel for the service
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "App Lock Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Shows when an app is locked and requires quiz completion to unlock");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    /**
     * Create notification for the service
     */
    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, com.smartappgatekeeper.ui.activities.MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Locked: " + targetAppName)
            .setContentText("Complete a quiz to unlock the app")
            .setSmallIcon(R.drawable.ic_quiz)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build();
    }
}

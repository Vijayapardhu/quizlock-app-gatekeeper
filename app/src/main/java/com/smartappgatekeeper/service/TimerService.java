package com.smartappgatekeeper.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.activities.MainActivity;

/**
 * Foreground service for managing app unlock timers
 * FR-011: System shall start timer when app is unlocked
 * FR-012: System shall display remaining time in overlay
 * FR-013: System shall lock app when timer expires
 */
public class TimerService extends Service {
    private static final String TAG = "TimerService";
    private static final String CHANNEL_ID = "TimerServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    
    private CountDownTimer countDownTimer;
    private long remainingTimeMillis;
    private String targetAppName;
    private String packageName;
    private android.view.View blockingView;
    private android.view.WindowManager windowManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d(TAG, "TimerService created");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            
            if ("START_TIMER".equals(action)) {
                startTimer(intent);
            } else if ("STOP_TIMER".equals(action)) {
                stopTimer();
            } else if ("UPDATE_TIMER".equals(action)) {
                updateTimer(intent);
            }
        }
        
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.d(TAG, "TimerService destroyed");
    }
    
    /**
     * Start timer for app unlock session
     */
    private void startTimer(Intent intent) {
        int durationMinutes = intent.getIntExtra("duration_minutes", 10);
        targetAppName = intent.getStringExtra("app_name");
        packageName = intent.getStringExtra("package_name");
        
        remainingTimeMillis = durationMinutes * 60 * 1000L;
        
        // Cancel existing timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        // Start foreground service
        startForeground(NOTIFICATION_ID, createNotification());
        
        // Create countdown timer
        countDownTimer = new CountDownTimer(remainingTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished;
                updateNotification();
            }
            
            @Override
            public void onFinish() {
                Log.d(TAG, "Timer finished for " + targetAppName);
                onTimerExpired();
            }
        };
        
        countDownTimer.start();
        Log.d(TAG, "Timer started for " + targetAppName + " (" + durationMinutes + " minutes)");
    }
    
    /**
     * Stop the current timer
     */
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        
        stopForeground(true);
        stopSelf();
        Log.d(TAG, "Timer stopped");
    }
    
    /**
     * Update timer with new duration
     */
    private void updateTimer(Intent intent) {
        int durationMinutes = intent.getIntExtra("duration_minutes", 10);
        remainingTimeMillis = durationMinutes * 60 * 1000L;
        
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = new CountDownTimer(remainingTimeMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeMillis = millisUntilFinished;
                    updateNotification();
                }
                
                @Override
                public void onFinish() {
                    onTimerExpired();
                }
            };
            countDownTimer.start();
        }
    }
    
    /**
     * Handle timer expiration
     */
    private void onTimerExpired() {
        // Implement app locking logic
        lockTargetApp();
        
        // Log the timer expiration event
        logTimerExpiration();
        
        // Show notification that app is now locked
        showAppLockedNotification();
        
        // Update usage statistics
        updateUsageStatistics();
        
        // Stop the service
        stopSelf();
    }
    
    /**
     * Lock the target app by starting the AppLockService
     */
    private void lockTargetApp() {
        try {
            // Start the app lock service
            startAppLockService();
            
            // Show notification
            showAppLockedNotification();
            
            // Log the event
            logTimerExpiration();
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error locking app", e);
        }
    }
    
    /**
     * Start the app lock service to monitor and block the target app
     */
    private void startAppLockService() {
        Intent lockIntent = new Intent(this, AppLockService.class);
        lockIntent.putExtra("target_package", packageName);
        lockIntent.putExtra("target_app_name", targetAppName);
        startService(lockIntent);
    }
    
    /**
     * Legacy method - kept for compatibility but now delegates to AppLockService
     */
    private void createLegacyBlockingView() {
        try {
            // Create a system alert window to block the app
            android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
            params.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                          android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                          android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            params.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            params.height = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            params.format = android.graphics.PixelFormat.TRANSLUCENT;
            
            // Create blocking view
            android.view.View blockingView = createBlockingView();
            
            // Add to window manager
            android.view.WindowManager windowManager = (android.view.WindowManager) getSystemService(WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.addView(blockingView, params);
                
                // Store reference for later removal
                this.blockingView = blockingView;
                this.windowManager = windowManager;
                
                android.util.Log.d(TAG, "App locked successfully");
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error locking app", e);
        }
    }
    
    /**
     * Create a blocking view that covers the entire screen
     */
    private android.view.View createBlockingView() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setBackgroundColor(android.graphics.Color.BLACK);
        layout.setGravity(android.view.Gravity.CENTER);
        
        // Add app locked message
        android.widget.TextView messageView = new android.widget.TextView(this);
        messageView.setText("🔒 App Locked\n\nTime's up! Complete a quiz to unlock.");
        messageView.setTextColor(android.graphics.Color.WHITE);
        messageView.setTextSize(18);
        messageView.setGravity(android.view.Gravity.CENTER);
        messageView.setPadding(50, 50, 50, 50);
        
        // Add unlock button
        android.widget.Button unlockButton = new android.widget.Button(this);
        unlockButton.setText("Take Quiz to Unlock");
        unlockButton.setTextColor(android.graphics.Color.WHITE);
        unlockButton.setBackgroundColor(android.graphics.Color.parseColor("#6200EE"));
        unlockButton.setOnClickListener(v -> {
            // Start quiz activity
            android.content.Intent quizIntent = new android.content.Intent(this, com.smartappgatekeeper.ui.activities.QuizActivity.class);
            quizIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(quizIntent);
            
            // Remove blocking view
            removeBlockingView();
        });
        
        layout.addView(messageView);
        layout.addView(unlockButton);
        
        return layout;
    }
    
    /**
     * Remove the blocking view
     */
    private void removeBlockingView() {
        if (blockingView != null && windowManager != null) {
            try {
                windowManager.removeView(blockingView);
                blockingView = null;
                windowManager = null;
                android.util.Log.d(TAG, "App unlocked successfully");
            } catch (Exception e) {
                android.util.Log.e(TAG, "Error removing blocking view", e);
            }
        }
    }
    
    /**
     * Log timer expiration event
     */
    private void logTimerExpiration() {
        android.util.Log.i(TAG, "Timer expired for app: " + packageName);
        // TODO: Log to analytics or database
    }
    
    /**
     * Show notification that app is locked
     */
    private void showAppLockedNotification() {
        android.app.NotificationManager notificationManager = 
            (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                CHANNEL_ID, "App Lock Notifications", 
                android.app.NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        
        android.content.Intent notificationIntent = new android.content.Intent(this, 
            com.smartappgatekeeper.ui.activities.QuizActivity.class);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivity(
            this, 0, notificationIntent, android.app.PendingIntent.FLAG_IMMUTABLE);
        
        android.app.Notification notification = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🔒 App Locked")
            .setContentText("Time's up! Complete a quiz to unlock " + packageName)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .build();
        
        notificationManager.notify(2, notification);
    }
    
    /**
     * Update usage statistics
     */
    private void updateUsageStatistics() {
        // TODO: Update usage statistics in database
        android.util.Log.d(TAG, "Updating usage statistics for timer expiration");
    }
    
    /**
     * Create notification channel for Android O and above
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Timer Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Shows remaining time for unlocked apps");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    /**
     * Create notification for timer service
     */
    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Unlocked: " + targetAppName)
            .setContentText("Time remaining: " + formatTime(remainingTimeMillis))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build();
    }
    
    /**
     * Update notification with current time
     */
    private void updateNotification() {
        Notification notification = createNotification();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, notification);
        }
    }
    
    /**
     * Format time in MM:SS format
     */
    private String formatTime(long millis) {
        long minutes = millis / (1000 * 60);
        long seconds = (millis % (1000 * 60)) / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
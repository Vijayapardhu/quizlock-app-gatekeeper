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
        // TODO: Implement app locking logic
        // This would involve:
        // 1. Log the timer expiration event
        // 2. Show notification that app is now locked
        // 3. Update usage statistics
        // 4. Stop the service
        
        Log.d(TAG, "App " + targetAppName + " is now locked");
        
        // Stop the service
        stopForeground(true);
        stopSelf();
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
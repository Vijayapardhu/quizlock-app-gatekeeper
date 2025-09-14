package com.smartappgatekeeper.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.activities.MainActivity;

import java.util.Random;

/**
 * Real-time notification service for live updates
 */
public class RealtimeNotificationService extends Service {
    
    private static final String TAG = "RealtimeNotificationService";
    private static final String CHANNEL_ID = "realtime_notifications";
    private static final int NOTIFICATION_ID = 1001;
    
    private Handler handler;
    private Runnable notificationRunnable;
    private boolean isRunning = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        createNotificationChannel();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification("Real-time updates active"));
        startRealtimeNotifications();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Real-time Notifications",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Live updates and notifications");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    private Notification createNotification(String content) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Smart App Gatekeeper")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build();
    }
    
    private void startRealtimeNotifications() {
        if (isRunning) return;
        
        isRunning = true;
        notificationRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    sendRealtimeNotification();
                    handler.postDelayed(this, 30000); // Send notification every 30 seconds
                }
            }
        };
        handler.post(notificationRunnable);
    }
    
    private void sendRealtimeNotification() {
        String[] notifications = {
            "🎯 New learning goal achieved!",
            "⚡ You've earned 50 coins!",
            "📈 Your accuracy improved to 85%",
            "🏆 New achievement unlocked!",
            "👥 Your friend completed a quiz!",
            "📊 Weekly progress: 75% complete",
            "🔥 5-day streak maintained!",
            "💡 New quiz questions available!",
            "🎨 New theme unlocked!",
            "📱 App usage within limits today"
        };
        
        Random random = new Random();
        String message = notifications[random.nextInt(notifications.length)];
        
        Notification notification = createNotification(message);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, notification);
        }
        
        Log.d(TAG, "Sent real-time notification: " + message);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (notificationRunnable != null) {
            handler.removeCallbacks(notificationRunnable);
        }
    }
}

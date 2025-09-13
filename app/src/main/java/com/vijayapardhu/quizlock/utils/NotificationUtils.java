package com.vijayapardhu.quizlock.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.vijayapardhu.quizlock.R;
import com.vijayapardhu.quizlock.ui.MainActivity;

public class NotificationUtils {
    
    private static final String CHANNEL_ID_SESSION = "session_channel";
    private static final String CHANNEL_ID_REMINDERS = "reminders_channel";
    
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = 
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            // Session notifications channel
            NotificationChannel sessionChannel = new NotificationChannel(
                CHANNEL_ID_SESSION,
                "Session Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            sessionChannel.setDescription("Notifications about active app sessions");
            notificationManager.createNotificationChannel(sessionChannel);
            
            // Reminder notifications channel
            NotificationChannel reminderChannel = new NotificationChannel(
                CHANNEL_ID_REMINDERS,
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_LOW
            );
            reminderChannel.setDescription("Streak and goal reminders");
            notificationManager.createNotificationChannel(reminderChannel);
        }
    }
    
    public static void showSessionActiveNotification(Context context, String appName, 
                                                   int remainingMinutes) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_SESSION)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle("Session Active - " + appName)
            .setContentText(remainingMinutes + " minutes remaining")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(false);
        
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID_SESSION_ACTIVE, builder.build());
    }
    
    public static void showSessionEndedNotification(Context context, String appName) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_SESSION)
            .setSmallIcon(R.drawable.ic_lock)
            .setContentTitle("Session Ended")
            .setContentText(appName + " has been locked again")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
        
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID_SESSION_ENDED, builder.build());
    }
    
    public static void showStreakReminderNotification(Context context, int currentStreak) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        String title = currentStreak > 0 ? 
            "Keep your streak going!" : 
            "Start your learning streak!";
        String content = currentStreak > 0 ? 
            "You're on a " + currentStreak + " day streak. Answer a quiz today!" :
            "Answer your first quiz to start a streak!";
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_streak)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
        
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID_STREAK_REMINDER, builder.build());
    }
    
    public static void cancelSessionNotification(Context context) {
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_ID_SESSION_ACTIVE);
    }
    
    public static Notification createSessionNotification(Context context, String appName, int durationMinutes) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        return new NotificationCompat.Builder(context, CHANNEL_ID_SESSION)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle("Session Active - " + appName)
            .setContentText(durationMinutes + " minutes session started")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .build();
    }
}
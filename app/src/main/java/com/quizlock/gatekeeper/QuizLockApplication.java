package com.quizlock.gatekeeper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.quizlock.gatekeeper.data.database.QuizLockDatabase;
import com.quizlock.gatekeeper.data.repository.QuizRepository;
import com.quizlock.gatekeeper.utils.PreferencesManager;

/**
 * Main Application class for QuizLock
 * Initializes database, repositories, and notification channels
 */
public class QuizLockApplication extends Application {
    
    public static final String NOTIFICATION_CHANNEL_ID = "quizlock_monitoring";
    private static final String NOTIFICATION_CHANNEL_NAME = "QuizLock Monitoring";
    
    private QuizLockDatabase database;
    private QuizRepository quizRepository;
    private PreferencesManager preferencesManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize components
        initializeDatabase();
        initializeRepositories();
        initializeNotificationChannels();
        initializeWorkManager();
        initializePreferences();
    }
    
    private void initializeDatabase() {
        database = QuizLockDatabase.getInstance(this);
    }
    
    private void initializeRepositories() {
        quizRepository = new QuizRepository(this, database);
    }
    
    private void initializeNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Notifications for app monitoring and quiz reminders");
            channel.setShowBadge(false);
            
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    
    private void initializeWorkManager() {
        Configuration config = new Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build();
        WorkManager.initialize(this, config);
    }
    
    private void initializePreferences() {
        preferencesManager = new PreferencesManager(this);
    }
    
    public QuizLockDatabase getDatabase() {
        return database;
    }
    
    public QuizRepository getQuizRepository() {
        return quizRepository;
    }
    
    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }
}
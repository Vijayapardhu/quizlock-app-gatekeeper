package com.smartappgatekeeper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.smartappgatekeeper.database.entities.AppSettings;

/**
 * Data Access Object for AppSettings entity
 * FR-033 to FR-037: Settings and Configuration requirements
 */
@Dao
public interface AppSettingsDao {
    
    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    LiveData<AppSettings> getAppSettings();
    
    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    AppSettings getAppSettingsSync();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppSettings(AppSettings appSettings);
    
    @Update
    void updateAppSettings(AppSettings appSettings);
    
    @Delete
    void deleteAppSettings(AppSettings appSettings);
    
    // Permission status updates
    @Query("UPDATE app_settings SET accessibilityServiceEnabled = :enabled WHERE id = 1")
    void updateAccessibilityServiceEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET usageStatsPermissionGranted = :granted WHERE id = 1")
    void updateUsageStatsPermissionGranted(boolean granted);
    
    @Query("UPDATE app_settings SET overlayPermissionGranted = :granted WHERE id = 1")
    void updateOverlayPermissionGranted(boolean granted);
    
    @Query("UPDATE app_settings SET notificationPermissionGranted = :granted WHERE id = 1")
    void updateNotificationPermissionGranted(boolean granted);
    
    @Query("UPDATE app_settings SET onboardingCompleted = :completed WHERE id = 1")
    void updateOnboardingCompleted(boolean completed);
    
    // Global app settings updates
    @Query("UPDATE app_settings SET darkModeEnabled = :enabled WHERE id = 1")
    void updateDarkModeEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET soundEnabled = :enabled WHERE id = 1")
    void updateSoundEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET vibrationEnabled = :enabled WHERE id = 1")
    void updateVibrationEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET analyticsEnabled = :enabled WHERE id = 1")
    void updateAnalyticsEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET language = :language WHERE id = 1")
    void updateLanguage(String language);
    
    // Notification settings updates
    @Query("UPDATE app_settings SET learningRemindersEnabled = :enabled WHERE id = 1")
    void updateLearningRemindersEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET progressUpdatesEnabled = :enabled WHERE id = 1")
    void updateProgressUpdatesEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET achievementNotificationsEnabled = :enabled WHERE id = 1")
    void updateAchievementNotificationsEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET weeklyReportsEnabled = :enabled WHERE id = 1")
    void updateWeeklyReportsEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET reminderTime = :time WHERE id = 1")
    void updateReminderTime(String time);
    
    // Emergency settings updates
    @Query("UPDATE app_settings SET emergencyBypassEnabled = :enabled WHERE id = 1")
    void updateEmergencyBypassEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET emergencyPassword = :password WHERE id = 1")
    void updateEmergencyPassword(String password);
    
    @Query("UPDATE app_settings SET emergencyUsesPerDay = :uses WHERE id = 1")
    void updateEmergencyUsesPerDay(int uses);
    
    @Query("UPDATE app_settings SET emergencyUsesUsed = :uses WHERE id = 1")
    void updateEmergencyUsesUsed(int uses);
    
    @Query("UPDATE app_settings SET emergencyResetDate = :date WHERE id = 1")
    void updateEmergencyResetDate(java.util.Date date);
    
    // Learning platform settings updates
    @Query("UPDATE app_settings SET selectedPlatformId = :platformId WHERE id = 1")
    void updateSelectedPlatformId(String platformId);
    
    @Query("UPDATE app_settings SET cloudSyncEnabled = :enabled WHERE id = 1")
    void updateCloudSyncEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET syncFrequency = :frequency WHERE id = 1")
    void updateSyncFrequency(String frequency);
    
    // Social features settings updates
    @Query("UPDATE app_settings SET socialFeaturesEnabled = :enabled WHERE id = 1")
    void updateSocialFeaturesEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET leaderboardEnabled = :enabled WHERE id = 1")
    void updateLeaderboardEnabled(boolean enabled);
    
    @Query("UPDATE app_settings SET friendRequestsEnabled = :enabled WHERE id = 1")
    void updateFriendRequestsEnabled(boolean enabled);
}
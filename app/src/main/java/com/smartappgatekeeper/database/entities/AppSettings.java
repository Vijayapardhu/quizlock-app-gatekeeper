package com.smartappgatekeeper.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entity representing app-wide settings and configuration
 * FR-033 to FR-037: Settings and Configuration requirements
 */
@Entity(tableName = "app_settings")
public class AppSettings {
    @PrimaryKey
    public int id = 1; // Singleton entity
    
    // Permission status
    public boolean accessibilityServiceEnabled;
    public boolean usageStatsPermissionGranted;
    public boolean overlayPermissionGranted;
    public boolean notificationPermissionGranted;
    public boolean onboardingCompleted;
    public Date createdAt;
    public Date lastUpdated;
    
    // Global app settings
    public boolean darkModeEnabled;
    public boolean soundEnabled;
    public boolean vibrationEnabled;
    public boolean analyticsEnabled;
    public boolean crashReportingEnabled;
    public String language; // "en", "es", etc.
    
    // Notification settings
    public boolean learningRemindersEnabled;
    public boolean progressUpdatesEnabled;
    public boolean achievementNotificationsEnabled;
    public boolean weeklyReportsEnabled;
    public String reminderTime; // "HH:mm" format
    
    // Emergency settings (FR-036)
    public boolean emergencyBypassEnabled;
    public String emergencyPassword; // Encrypted
    public int emergencyUsesPerDay;
    public int emergencyUsesUsed;
    public Date emergencyResetDate;
    
    // Learning platform settings
    public String selectedPlatformId;
    public boolean cloudSyncEnabled;
    public String syncFrequency; // "realtime", "hourly", "daily"
    
    // Social features settings
    public boolean socialFeaturesEnabled;
    public boolean leaderboardEnabled;
    public boolean friendRequestsEnabled;
    
    public AppSettings() {
        this.createdAt = new Date();
        this.lastUpdated = new Date();
        this.onboardingCompleted = false;
        this.darkModeEnabled = false;
        this.soundEnabled = true;
        this.vibrationEnabled = true;
        this.analyticsEnabled = true;
        this.crashReportingEnabled = true;
        this.language = "en";
        this.learningRemindersEnabled = true;
        this.progressUpdatesEnabled = true;
        this.achievementNotificationsEnabled = true;
        this.weeklyReportsEnabled = true;
        this.reminderTime = "09:00";
        this.emergencyBypassEnabled = false;
        this.emergencyUsesPerDay = 3;
        this.emergencyUsesUsed = 0;
        this.cloudSyncEnabled = false;
        this.syncFrequency = "daily";
        this.socialFeaturesEnabled = false;
        this.leaderboardEnabled = false;
        this.friendRequestsEnabled = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public boolean isAccessibilityServiceEnabled() { return accessibilityServiceEnabled; }
    public void setAccessibilityServiceEnabled(boolean accessibilityServiceEnabled) { this.accessibilityServiceEnabled = accessibilityServiceEnabled; }
    
    public boolean isUsageStatsPermissionGranted() { return usageStatsPermissionGranted; }
    public void setUsageStatsPermissionGranted(boolean usageStatsPermissionGranted) { this.usageStatsPermissionGranted = usageStatsPermissionGranted; }
    
    public boolean isOverlayPermissionGranted() { return overlayPermissionGranted; }
    public void setOverlayPermissionGranted(boolean overlayPermissionGranted) { this.overlayPermissionGranted = overlayPermissionGranted; }
    
    public boolean isNotificationPermissionGranted() { return notificationPermissionGranted; }
    public void setNotificationPermissionGranted(boolean notificationPermissionGranted) { this.notificationPermissionGranted = notificationPermissionGranted; }
    
    public boolean isOnboardingCompleted() { return onboardingCompleted; }
    public void setOnboardingCompleted(boolean onboardingCompleted) { this.onboardingCompleted = onboardingCompleted; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public boolean isDarkModeEnabled() { return darkModeEnabled; }
    public void setDarkModeEnabled(boolean darkModeEnabled) { this.darkModeEnabled = darkModeEnabled; }
    
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }
    
    public boolean isVibrationEnabled() { return vibrationEnabled; }
    public void setVibrationEnabled(boolean vibrationEnabled) { this.vibrationEnabled = vibrationEnabled; }
    
    public boolean isAnalyticsEnabled() { return analyticsEnabled; }
    public void setAnalyticsEnabled(boolean analyticsEnabled) { this.analyticsEnabled = analyticsEnabled; }
    
    public boolean isCrashReportingEnabled() { return crashReportingEnabled; }
    public void setCrashReportingEnabled(boolean crashReportingEnabled) { this.crashReportingEnabled = crashReportingEnabled; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public boolean isLearningRemindersEnabled() { return learningRemindersEnabled; }
    public void setLearningRemindersEnabled(boolean learningRemindersEnabled) { this.learningRemindersEnabled = learningRemindersEnabled; }
    
    public boolean isProgressUpdatesEnabled() { return progressUpdatesEnabled; }
    public void setProgressUpdatesEnabled(boolean progressUpdatesEnabled) { this.progressUpdatesEnabled = progressUpdatesEnabled; }
    
    public boolean isAchievementNotificationsEnabled() { return achievementNotificationsEnabled; }
    public void setAchievementNotificationsEnabled(boolean achievementNotificationsEnabled) { this.achievementNotificationsEnabled = achievementNotificationsEnabled; }
    
    public boolean isWeeklyReportsEnabled() { return weeklyReportsEnabled; }
    public void setWeeklyReportsEnabled(boolean weeklyReportsEnabled) { this.weeklyReportsEnabled = weeklyReportsEnabled; }
    
    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    
    public boolean isEmergencyBypassEnabled() { return emergencyBypassEnabled; }
    public void setEmergencyBypassEnabled(boolean emergencyBypassEnabled) { this.emergencyBypassEnabled = emergencyBypassEnabled; }
    
    public String getEmergencyPassword() { return emergencyPassword; }
    public void setEmergencyPassword(String emergencyPassword) { this.emergencyPassword = emergencyPassword; }
    
    public int getEmergencyUsesPerDay() { return emergencyUsesPerDay; }
    public void setEmergencyUsesPerDay(int emergencyUsesPerDay) { this.emergencyUsesPerDay = emergencyUsesPerDay; }
    
    public int getEmergencyUsesUsed() { return emergencyUsesUsed; }
    public void setEmergencyUsesUsed(int emergencyUsesUsed) { this.emergencyUsesUsed = emergencyUsesUsed; }
    
    public Date getEmergencyResetDate() { return emergencyResetDate; }
    public void setEmergencyResetDate(Date emergencyResetDate) { this.emergencyResetDate = emergencyResetDate; }
    
    public String getSelectedPlatformId() { return selectedPlatformId; }
    public void setSelectedPlatformId(String selectedPlatformId) { this.selectedPlatformId = selectedPlatformId; }
    
    public boolean isCloudSyncEnabled() { return cloudSyncEnabled; }
    public void setCloudSyncEnabled(boolean cloudSyncEnabled) { this.cloudSyncEnabled = cloudSyncEnabled; }
    
    public String getSyncFrequency() { return syncFrequency; }
    public void setSyncFrequency(String syncFrequency) { this.syncFrequency = syncFrequency; }
    
    public boolean isSocialFeaturesEnabled() { return socialFeaturesEnabled; }
    public void setSocialFeaturesEnabled(boolean socialFeaturesEnabled) { this.socialFeaturesEnabled = socialFeaturesEnabled; }
    
    public boolean isLeaderboardEnabled() { return leaderboardEnabled; }
    public void setLeaderboardEnabled(boolean leaderboardEnabled) { this.leaderboardEnabled = leaderboardEnabled; }
    
    public boolean isFriendRequestsEnabled() { return friendRequestsEnabled; }
    public void setFriendRequestsEnabled(boolean friendRequestsEnabled) { this.friendRequestsEnabled = friendRequestsEnabled; }
    
    // Additional methods for compatibility
    public boolean isNotificationsEnabled() { return learningRemindersEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.learningRemindersEnabled = notificationsEnabled; }
    
    public String getQuizDifficulty() { return "medium"; } // Default value
    public void setQuizDifficulty(String quizDifficulty) { } // No-op for now
    
    public int getSessionDuration() { return 30; } // Default 30 minutes
    public void setSessionDuration(int sessionDuration) { } // No-op for now
}
package com.quizlock.gatekeeper.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Entity representing app usage statistics
 */
@Entity(tableName = "app_usage")
public class AppUsage {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "package_name")
    private String packageName;
    
    @ColumnInfo(name = "app_name")
    private String appName;
    
    @ColumnInfo(name = "usage_time_ms")
    private long usageTimeMs; // Total time spent in app in milliseconds
    
    @ColumnInfo(name = "launch_count")
    private int launchCount; // Number of times app was launched
    
    @ColumnInfo(name = "blocked_attempts")
    private int blockedAttempts; // Number of times access was blocked
    
    @ColumnInfo(name = "successful_unlocks")
    private int successfulUnlocks; // Number of times quiz was answered correctly
    
    @ColumnInfo(name = "last_used")
    private long lastUsed;
    
    @ColumnInfo(name = "last_unlock")
    private long lastUnlock; // When app was last unlocked via quiz
    
    @ColumnInfo(name = "unlock_duration_ms")
    private long unlockDurationMs; // How long app stays unlocked
    
    @ColumnInfo(name = "is_blocked")
    private boolean isBlocked; // Whether this app is currently being monitored
    
    @ColumnInfo(name = "date")
    private String date; // Date in YYYY-MM-DD format for daily stats
    
    // Constructors
    public AppUsage() {
        this.lastUsed = System.currentTimeMillis();
        this.date = getDateString(this.lastUsed);
    }
    
    public AppUsage(String packageName, String appName, boolean isBlocked) {
        this.packageName = packageName;
        this.appName = appName;
        this.isBlocked = isBlocked;
        this.usageTimeMs = 0;
        this.launchCount = 0;
        this.blockedAttempts = 0;
        this.successfulUnlocks = 0;
        this.lastUsed = System.currentTimeMillis();
        this.lastUnlock = 0;
        this.unlockDurationMs = 15 * 60 * 1000; // Default 15 minutes
        this.date = getDateString(this.lastUsed);
    }
    
    // Helper method to get date string
    private String getDateString(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    
    public long getUsageTimeMs() { return usageTimeMs; }
    public void setUsageTimeMs(long usageTimeMs) { this.usageTimeMs = usageTimeMs; }
    
    public int getLaunchCount() { return launchCount; }
    public void setLaunchCount(int launchCount) { this.launchCount = launchCount; }
    
    public int getBlockedAttempts() { return blockedAttempts; }
    public void setBlockedAttempts(int blockedAttempts) { this.blockedAttempts = blockedAttempts; }
    
    public int getSuccessfulUnlocks() { return successfulUnlocks; }
    public void setSuccessfulUnlocks(int successfulUnlocks) { this.successfulUnlocks = successfulUnlocks; }
    
    public long getLastUsed() { return lastUsed; }
    public void setLastUsed(long lastUsed) { this.lastUsed = lastUsed; }
    
    public long getLastUnlock() { return lastUnlock; }
    public void setLastUnlock(long lastUnlock) { this.lastUnlock = lastUnlock; }
    
    public long getUnlockDurationMs() { return unlockDurationMs; }
    public void setUnlockDurationMs(long unlockDurationMs) { this.unlockDurationMs = unlockDurationMs; }
    
    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    // Helper methods
    public boolean isCurrentlyUnlocked() {
        if (lastUnlock == 0) return false;
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUnlock) < unlockDurationMs;
    }
    
    public long getRemainingUnlockTime() {
        if (!isCurrentlyUnlocked()) return 0;
        long currentTime = System.currentTimeMillis();
        return unlockDurationMs - (currentTime - lastUnlock);
    }
    
    public void incrementLaunchCount() {
        this.launchCount++;
        this.lastUsed = System.currentTimeMillis();
    }
    
    public void incrementBlockedAttempts() {
        this.blockedAttempts++;
    }
    
    public void incrementSuccessfulUnlocks() {
        this.successfulUnlocks++;
        this.lastUnlock = System.currentTimeMillis();
    }
}
package com.vijayapardhu.quizlock.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restricted_apps")
public class RestrictedApp {
    @PrimaryKey
    public String packageName;
    
    public String appName;
    public String iconPath;
    public boolean isRestricted;
    public int dailyLimitMinutes;
    public int usedTimeMinutes;
    public long lastResetTimestamp;
    
    public RestrictedApp() {}
    
    public RestrictedApp(String packageName, String appName, String iconPath, 
                        boolean isRestricted, int dailyLimitMinutes) {
        this.packageName = packageName;
        this.appName = appName;
        this.iconPath = iconPath;
        this.isRestricted = isRestricted;
        this.dailyLimitMinutes = dailyLimitMinutes;
        this.usedTimeMinutes = 0;
        this.lastResetTimestamp = System.currentTimeMillis();
    }
    
    // Getters and setters
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    
    public String getIconPath() { return iconPath; }
    public void setIconPath(String iconPath) { this.iconPath = iconPath; }
    
    public boolean isRestricted() { return isRestricted; }
    public void setRestricted(boolean restricted) { isRestricted = restricted; }
    
    public int getDailyLimitMinutes() { return dailyLimitMinutes; }
    public void setDailyLimitMinutes(int dailyLimitMinutes) { this.dailyLimitMinutes = dailyLimitMinutes; }
    
    public int getUsedTimeMinutes() { return usedTimeMinutes; }
    public void setUsedTimeMinutes(int usedTimeMinutes) { this.usedTimeMinutes = usedTimeMinutes; }
    
    public long getLastResetTimestamp() { return lastResetTimestamp; }
    public void setLastResetTimestamp(long lastResetTimestamp) { this.lastResetTimestamp = lastResetTimestamp; }
}
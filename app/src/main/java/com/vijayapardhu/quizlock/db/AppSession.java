package com.vijayapardhu.quizlock.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "app_sessions")
public class AppSession {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String packageName;
    public long startTime;
    public long endTime;
    public int durationMinutes;
    public boolean wasQuizAnswered;
    public boolean wasEmergencyUnlock;
    public String date; // Format: yyyy-MM-dd
    
    public AppSession() {}
    
    public AppSession(String packageName, long startTime, boolean wasQuizAnswered, 
                     boolean wasEmergencyUnlock, String date) {
        this.packageName = packageName;
        this.startTime = startTime;
        this.wasQuizAnswered = wasQuizAnswered;
        this.wasEmergencyUnlock = wasEmergencyUnlock;
        this.date = date;
        this.endTime = 0;
        this.durationMinutes = 0;
    }
    
    public void endSession() {
        this.endTime = System.currentTimeMillis();
        this.durationMinutes = (int) ((endTime - startTime) / (1000 * 60));
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public boolean isWasQuizAnswered() { return wasQuizAnswered; }
    public void setWasQuizAnswered(boolean wasQuizAnswered) { this.wasQuizAnswered = wasQuizAnswered; }
    
    public boolean isWasEmergencyUnlock() { return wasEmergencyUnlock; }
    public void setWasEmergencyUnlock(boolean wasEmergencyUnlock) { this.wasEmergencyUnlock = wasEmergencyUnlock; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
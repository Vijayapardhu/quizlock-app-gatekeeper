package com.smartappgatekeeper.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entity representing user learning streaks
 * FR-030: System shall track daily and weekly streaks
 */
@Entity(tableName = "streaks")
public class Streak {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String streakType; // "daily", "weekly", "monthly"
    public int currentCount;
    public int longestCount;
    public Date startDate;
    public Date lastActivityDate;
    public Date createdAt;
    public Date lastUpdated;
    public long lastUpdateTime; // Timestamp for easier access
    
    // Streak metadata
    public boolean isActive;
    public int totalQuestionsAnswered;
    public int totalCorrectAnswers;
    public int totalCoinsEarned;
    public String achievements; // JSON string of unlocked achievements
    
    public Streak() {
        this.createdAt = new Date();
        this.lastUpdated = new Date();
        this.lastUpdateTime = System.currentTimeMillis();
        this.currentCount = 0;
        this.longestCount = 0;
        this.isActive = false;
        this.totalQuestionsAnswered = 0;
        this.totalCorrectAnswers = 0;
        this.totalCoinsEarned = 0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getStreakType() { return streakType; }
    public void setStreakType(String streakType) { this.streakType = streakType; }
    
    public int getCurrentCount() { return currentCount; }
    public void setCurrentCount(int currentCount) { this.currentCount = currentCount; }
    
    public int getCount() { return currentCount; }
    public void setCount(int count) { this.currentCount = count; }
    
    public int getLongestCount() { return longestCount; }
    public void setLongestCount(int longestCount) { this.longestCount = longestCount; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(Date lastActivityDate) { this.lastActivityDate = lastActivityDate; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public long getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public int getTotalQuestionsAnswered() { return totalQuestionsAnswered; }
    public void setTotalQuestionsAnswered(int totalQuestionsAnswered) { this.totalQuestionsAnswered = totalQuestionsAnswered; }
    
    public int getTotalCorrectAnswers() { return totalCorrectAnswers; }
    public void setTotalCorrectAnswers(int totalCorrectAnswers) { this.totalCorrectAnswers = totalCorrectAnswers; }
    
    public int getTotalCoinsEarned() { return totalCoinsEarned; }
    public void setTotalCoinsEarned(int totalCoinsEarned) { this.totalCoinsEarned = totalCoinsEarned; }
    
    public String getAchievements() { return achievements; }
    public void setAchievements(String achievements) { this.achievements = achievements; }
}
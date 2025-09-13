package com.smartappgatekeeper.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entity representing user profile and preferences
 * FR-033: System shall allow target app selection and configuration
 */
@Entity(tableName = "user_profile")
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String name;
    public String email;
    public String selectedTopics; // JSON string of selected topics
    public int difficultyLevel; // 1=Easy, 2=Medium, 3=Hard
    public Date createdAt;
    public Date lastUpdated;
    
    // Learning preferences
    public int dailyGoalMinutes;
    public boolean notificationsEnabled;
    public String reminderTime; // HH:mm format
    public String language;
    
    // Social features
    public boolean socialFeaturesEnabled;
    public String friendCode; // Unique code for friend connections
    
    // Additional fields for compatibility
    public int age;
    public String goal;
    public long joinDate;
    public int coins;
    
    public UserProfile() {
        this.createdAt = new Date();
        this.lastUpdated = new Date();
        this.difficultyLevel = 1; // Easy by default
        this.dailyGoalMinutes = 30; // 30 minutes default
        this.notificationsEnabled = true;
        this.socialFeaturesEnabled = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSelectedTopics() { return selectedTopics; }
    public void setSelectedTopics(String selectedTopics) { this.selectedTopics = selectedTopics; }
    
    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public int getDailyGoalMinutes() { return dailyGoalMinutes; }
    public void setDailyGoalMinutes(int dailyGoalMinutes) { this.dailyGoalMinutes = dailyGoalMinutes; }
    
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    
    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public boolean isSocialFeaturesEnabled() { return socialFeaturesEnabled; }
    public void setSocialFeaturesEnabled(boolean socialFeaturesEnabled) { this.socialFeaturesEnabled = socialFeaturesEnabled; }
    
    public String getFriendCode() { return friendCode; }
    public void setFriendCode(String friendCode) { this.friendCode = friendCode; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    
    public long getJoinDate() { return joinDate; }
    public void setJoinDate(long joinDate) { this.joinDate = joinDate; }
    
    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }
}
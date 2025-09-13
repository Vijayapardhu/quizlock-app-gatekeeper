package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class UserProfileRequest {
    @SerializedName("username")
    private String username;
    
    @SerializedName("selected_topics")
    private String selectedTopics;
    
    @SerializedName("difficulty_level")
    private int difficultyLevel;
    
    @SerializedName("daily_goal_minutes")
    private int dailyGoalMinutes;
    
    @SerializedName("notifications_enabled")
    private boolean notificationsEnabled;
    
    @SerializedName("social_features_enabled")
    private boolean socialFeaturesEnabled;

    // Constructors
    public UserProfileRequest() {}

    public UserProfileRequest(String username, String selectedTopics, int difficultyLevel, 
                            int dailyGoalMinutes, boolean notificationsEnabled, boolean socialFeaturesEnabled) {
        this.username = username;
        this.selectedTopics = selectedTopics;
        this.difficultyLevel = difficultyLevel;
        this.dailyGoalMinutes = dailyGoalMinutes;
        this.notificationsEnabled = notificationsEnabled;
        this.socialFeaturesEnabled = socialFeaturesEnabled;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getSelectedTopics() { return selectedTopics; }
    public void setSelectedTopics(String selectedTopics) { this.selectedTopics = selectedTopics; }
    
    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public int getDailyGoalMinutes() { return dailyGoalMinutes; }
    public void setDailyGoalMinutes(int dailyGoalMinutes) { this.dailyGoalMinutes = dailyGoalMinutes; }
    
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    
    public boolean isSocialFeaturesEnabled() { return socialFeaturesEnabled; }
    public void setSocialFeaturesEnabled(boolean socialFeaturesEnabled) { this.socialFeaturesEnabled = socialFeaturesEnabled; }
}

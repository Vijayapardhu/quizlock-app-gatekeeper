package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("user_profile")
    private UserProfileData userProfile;

    // Constructors
    public UserProfileResponse() {}

    public UserProfileResponse(boolean success, String message, UserProfileData userProfile) {
        this.success = success;
        this.message = message;
        this.userProfile = userProfile;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserProfileData getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileData userProfile) {
        this.userProfile = userProfile;
    }

    public static class UserProfileData {
        @SerializedName("id")
        private String id;
        
        @SerializedName("username")
        private String username;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("level")
        private int level;
        
        @SerializedName("experience")
        private int experience;
        
        @SerializedName("coins")
        private int coins;
        
        @SerializedName("selected_topics")
        private String selectedTopics;
        
        @SerializedName("difficulty_level")
        private int difficultyLevel;
        
        @SerializedName("daily_goal_minutes")
        private int dailyGoalMinutes;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        
        public int getExperience() { return experience; }
        public void setExperience(int experience) { this.experience = experience; }
        
        public int getCoins() { return coins; }
        public void setCoins(int coins) { this.coins = coins; }
        
        public String getSelectedTopics() { return selectedTopics; }
        public void setSelectedTopics(String selectedTopics) { this.selectedTopics = selectedTopics; }
        
        public int getDifficultyLevel() { return difficultyLevel; }
        public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
        
        public int getDailyGoalMinutes() { return dailyGoalMinutes; }
        public void setDailyGoalMinutes(int dailyGoalMinutes) { this.dailyGoalMinutes = dailyGoalMinutes; }
    }
}

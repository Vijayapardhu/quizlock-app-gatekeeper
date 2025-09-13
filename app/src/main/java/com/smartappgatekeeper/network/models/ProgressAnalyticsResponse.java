package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class ProgressAnalyticsResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("progress")
    private ProgressData progress;

    public static class ProgressData {
        @SerializedName("current_level")
        private int currentLevel;
        
        @SerializedName("experience")
        private int experience;
        
        @SerializedName("level_progress")
        private double levelProgress;
        
        @SerializedName("weekly_progress")
        private double weeklyProgress;
        
        @SerializedName("monthly_progress")
        private double monthlyProgress;

        // Getters and Setters
        public int getCurrentLevel() { return currentLevel; }
        public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
        
        public int getExperience() { return experience; }
        public void setExperience(int experience) { this.experience = experience; }
        
        public double getLevelProgress() { return levelProgress; }
        public void setLevelProgress(double levelProgress) { this.levelProgress = levelProgress; }
        
        public double getWeeklyProgress() { return weeklyProgress; }
        public void setWeeklyProgress(double weeklyProgress) { this.weeklyProgress = weeklyProgress; }
        
        public double getMonthlyProgress() { return monthlyProgress; }
        public void setMonthlyProgress(double monthlyProgress) { this.monthlyProgress = monthlyProgress; }
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public ProgressData getProgress() { return progress; }
    public void setProgress(ProgressData progress) { this.progress = progress; }
}

package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class UserStatsResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("stats")
    private UserStatsData stats;

    public static class UserStatsData {
        @SerializedName("total_questions_answered")
        private int totalQuestionsAnswered;
        
        @SerializedName("correct_answers")
        private int correctAnswers;
        
        @SerializedName("accuracy_rate")
        private double accuracyRate;
        
        @SerializedName("current_streak")
        private int currentStreak;
        
        @SerializedName("longest_streak")
        private int longestStreak;
        
        @SerializedName("total_study_time")
        private int totalStudyTime;
        
        @SerializedName("level")
        private int level;
        
        @SerializedName("experience")
        private int experience;

        // Getters and Setters
        public int getTotalQuestionsAnswered() { return totalQuestionsAnswered; }
        public void setTotalQuestionsAnswered(int totalQuestionsAnswered) { this.totalQuestionsAnswered = totalQuestionsAnswered; }
        
        public int getCorrectAnswers() { return correctAnswers; }
        public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
        
        public double getAccuracyRate() { return accuracyRate; }
        public void setAccuracyRate(double accuracyRate) { this.accuracyRate = accuracyRate; }
        
        public int getCurrentStreak() { return currentStreak; }
        public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
        
        public int getLongestStreak() { return longestStreak; }
        public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }
        
        public int getTotalStudyTime() { return totalStudyTime; }
        public void setTotalStudyTime(int totalStudyTime) { this.totalStudyTime = totalStudyTime; }
        
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        
        public int getExperience() { return experience; }
        public void setExperience(int experience) { this.experience = experience; }
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public UserStatsData getStats() { return stats; }
    public void setStats(UserStatsData stats) { this.stats = stats; }
}

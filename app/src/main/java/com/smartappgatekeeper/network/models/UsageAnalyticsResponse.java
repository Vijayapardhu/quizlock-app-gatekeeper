package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class UsageAnalyticsResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("analytics")
    private UsageAnalyticsData analytics;

    public static class UsageAnalyticsData {
        @SerializedName("total_app_opens")
        private int totalAppOpens;
        
        @SerializedName("total_quiz_sessions")
        private int totalQuizSessions;
        
        @SerializedName("total_study_time")
        private long totalStudyTime;
        
        @SerializedName("most_used_topics")
        private String mostUsedTopics;
        
        @SerializedName("streak_days")
        private int streakDays;

        // Getters and Setters
        public int getTotalAppOpens() { return totalAppOpens; }
        public void setTotalAppOpens(int totalAppOpens) { this.totalAppOpens = totalAppOpens; }
        
        public int getTotalQuizSessions() { return totalQuizSessions; }
        public void setTotalQuizSessions(int totalQuizSessions) { this.totalQuizSessions = totalQuizSessions; }
        
        public long getTotalStudyTime() { return totalStudyTime; }
        public void setTotalStudyTime(long totalStudyTime) { this.totalStudyTime = totalStudyTime; }
        
        public String getMostUsedTopics() { return mostUsedTopics; }
        public void setMostUsedTopics(String mostUsedTopics) { this.mostUsedTopics = mostUsedTopics; }
        
        public int getStreakDays() { return streakDays; }
        public void setStreakDays(int streakDays) { this.streakDays = streakDays; }
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public UsageAnalyticsData getAnalytics() { return analytics; }
    public void setAnalytics(UsageAnalyticsData analytics) { this.analytics = analytics; }
}

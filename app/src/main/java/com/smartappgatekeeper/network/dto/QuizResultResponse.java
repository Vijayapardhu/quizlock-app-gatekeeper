package com.smartappgatekeeper.network.dto;

import java.util.List;

/**
 * Quiz result response DTO
 */
public class QuizResultResponse {
    private String quizId;
    private int score;
    private int totalQuestions;
    private double accuracy;
    private int timeSpent; // in seconds
    private int coinsEarned;
    private int timeSaved; // in minutes
    private List<AchievementUnlocked> achievementsUnlocked;
    private String message;
    private boolean success;
    
    public QuizResultResponse() {}
    
    public QuizResultResponse(String quizId, int score, int totalQuestions, double accuracy, 
                            int timeSpent, int coinsEarned, int timeSaved, 
                            List<AchievementUnlocked> achievementsUnlocked, String message, boolean success) {
        this.quizId = quizId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.accuracy = accuracy;
        this.timeSpent = timeSpent;
        this.coinsEarned = coinsEarned;
        this.timeSaved = timeSaved;
        this.achievementsUnlocked = achievementsUnlocked;
        this.message = message;
        this.success = success;
    }
    
    // Getters and setters
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
    
    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    
    public int getCoinsEarned() { return coinsEarned; }
    public void setCoinsEarned(int coinsEarned) { this.coinsEarned = coinsEarned; }
    
    public int getTimeSaved() { return timeSaved; }
    public void setTimeSaved(int timeSaved) { this.timeSaved = timeSaved; }
    
    public List<AchievementUnlocked> getAchievementsUnlocked() { return achievementsUnlocked; }
    public void setAchievementsUnlocked(List<AchievementUnlocked> achievementsUnlocked) { 
        this.achievementsUnlocked = achievementsUnlocked; 
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    /**
     * Achievement unlocked inner class
     */
    public static class AchievementUnlocked {
        private String id;
        private String name;
        private String description;
        private String category;
        private int coinsReward;
        
        public AchievementUnlocked() {}
        
        public AchievementUnlocked(String id, String name, String description, String category, int coinsReward) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.coinsReward = coinsReward;
        }
        
        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public int getCoinsReward() { return coinsReward; }
        public void setCoinsReward(int coinsReward) { this.coinsReward = coinsReward; }
    }
}

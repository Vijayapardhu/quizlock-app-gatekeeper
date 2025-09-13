package com.quizlock.gatekeeper.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Entity representing user achievements and streaks
 */
@Entity(tableName = "user_stats")
public class UserStats {
    
    @PrimaryKey
    private int id = 1; // Single row for user stats
    
    @ColumnInfo(name = "current_streak")
    private int currentStreak;
    
    @ColumnInfo(name = "longest_streak")
    private int longestStreak;
    
    @ColumnInfo(name = "total_questions_answered")
    private int totalQuestionsAnswered;
    
    @ColumnInfo(name = "correct_answers")
    private int correctAnswers;
    
    @ColumnInfo(name = "total_time_saved_ms")
    private long totalTimeSavedMs; // Time prevented from being wasted
    
    @ColumnInfo(name = "total_study_time_ms")
    private long totalStudyTimeMs; // Time spent answering questions
    
    @ColumnInfo(name = "last_activity_date")
    private String lastActivityDate; // Last date user answered a question
    
    @ColumnInfo(name = "level")
    private int level; // User level based on experience
    
    @ColumnInfo(name = "experience_points")
    private int experiencePoints;
    
    @ColumnInfo(name = "badges_earned")
    private String badgesEarned; // Comma-separated list of badge IDs
    
    @ColumnInfo(name = "favorite_category")
    private String favoriteCategory;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    @ColumnInfo(name = "updated_at")
    private long updatedAt;
    
    // Constructors
    public UserStats() {
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.totalQuestionsAnswered = 0;
        this.correctAnswers = 0;
        this.totalTimeSavedMs = 0;
        this.totalStudyTimeMs = 0;
        this.lastActivityDate = getCurrentDateString();
        this.level = 1;
        this.experiencePoints = 0;
        this.badgesEarned = "";
        this.favoriteCategory = "general";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    // Helper method to get current date string
    private String getCurrentDateString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    
    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }
    
    public int getTotalQuestionsAnswered() { return totalQuestionsAnswered; }
    public void setTotalQuestionsAnswered(int totalQuestionsAnswered) { this.totalQuestionsAnswered = totalQuestionsAnswered; }
    
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public long getTotalTimeSavedMs() { return totalTimeSavedMs; }
    public void setTotalTimeSavedMs(long totalTimeSavedMs) { this.totalTimeSavedMs = totalTimeSavedMs; }
    
    public long getTotalStudyTimeMs() { return totalStudyTimeMs; }
    public void setTotalStudyTimeMs(long totalStudyTimeMs) { this.totalStudyTimeMs = totalStudyTimeMs; }
    
    public String getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(String lastActivityDate) { this.lastActivityDate = lastActivityDate; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getExperiencePoints() { return experiencePoints; }
    public void setExperiencePoints(int experiencePoints) { this.experiencePoints = experiencePoints; }
    
    public String getBadgesEarned() { return badgesEarned; }
    public void setBadgesEarned(String badgesEarned) { this.badgesEarned = badgesEarned; }
    
    public String getFavoriteCategory() { return favoriteCategory; }
    public void setFavoriteCategory(String favoriteCategory) { this.favoriteCategory = favoriteCategory; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods
    public double getAccuracyPercentage() {
        if (totalQuestionsAnswered == 0) return 0.0;
        return (double) correctAnswers / totalQuestionsAnswered * 100.0;
    }
    
    public void updateStreak(boolean isCorrectAnswer) {
        String today = getCurrentDateString();
        
        if (isCorrectAnswer) {
            if (today.equals(lastActivityDate)) {
                // Same day, don't change streak
            } else if (isConsecutiveDay(lastActivityDate, today)) {
                // Consecutive day, increment streak
                currentStreak++;
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                }
            } else {
                // Streak broken, reset to 1
                currentStreak = 1;
            }
            lastActivityDate = today;
        }
        
        updatedAt = System.currentTimeMillis();
    }
    
    public void addExperience(int points) {
        experiencePoints += points;
        
        // Level up calculation (100 points per level)
        int newLevel = (experiencePoints / 100) + 1;
        if (newLevel > level) {
            level = newLevel;
            // Could trigger level up event here
        }
        
        updatedAt = System.currentTimeMillis();
    }
    
    public void recordAnswer(boolean isCorrect, long timeTakenMs) {
        totalQuestionsAnswered++;
        totalStudyTimeMs += timeTakenMs;
        
        if (isCorrect) {
            correctAnswers++;
            addExperience(10); // 10 points for correct answer
            updateStreak(true);
        } else {
            updateStreak(false);
        }
        
        updatedAt = System.currentTimeMillis();
    }
    
    private boolean isConsecutiveDay(String lastDate, String currentDate) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            java.util.Date last = sdf.parse(lastDate);
            java.util.Date current = sdf.parse(currentDate);
            
            long diffInMs = current.getTime() - last.getTime();
            long diffInDays = diffInMs / (24 * 60 * 60 * 1000);
            
            return diffInDays == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
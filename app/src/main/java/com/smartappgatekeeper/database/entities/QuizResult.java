package com.smartappgatekeeper.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.Date;

/**
 * Entity representing quiz results and performance
 * FR-008: System shall track quiz performance and accuracy
 */
@Entity(tableName = "quiz_results")
@TypeConverters({com.smartappgatekeeper.database.converters.DateConverters.class})
public class QuizResult {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String quizId; // Unique identifier for the quiz session
    public String userId; // User who took the quiz
    public int totalQuestions; // Total number of questions
    public int correctAnswers; // Number of correct answers
    public int wrongAnswers; // Number of wrong answers
    public int skippedQuestions; // Number of skipped questions
    public double accuracy; // Accuracy percentage (0.0 to 100.0)
    public int score; // Final score
    public long timeSpent; // Time spent in milliseconds
    public String difficulty; // Quiz difficulty level
    public String topic; // Quiz topic/category
    public Date completedAt; // When the quiz was completed
    public long completedAtLong; // Long timestamp for compatibility
    
    // Quiz session details
    public String quizType; // "practice", "assessment", "challenge"
    public boolean isPassed; // Whether the quiz was passed
    public int passingScore; // Required score to pass
    public String status; // "completed", "abandoned", "timeout"
    
    // Performance metrics
    public double averageTimePerQuestion; // Average time per question in seconds
    public int streak; // Longest correct answer streak
    public String weakAreas; // JSON string of topics that need improvement
    public String strongAreas; // JSON string of topics that were strong
    
    // Rewards and achievements
    public int coinsEarned; // Coins earned from this quiz
    public int experiencePoints; // XP earned
    public String achievementsUnlocked; // JSON string of achievements unlocked
    public boolean isPersonalBest; // Whether this is a personal best score
    
    // Metadata
    public String deviceInfo; // Device information
    public String appVersion; // App version when quiz was taken
    public String notes; // Additional notes or comments
    public boolean isSynced; // Whether this result has been synced to backend
    
    // Constructors
    public QuizResult() {
        this.completedAt = new Date();
        this.completedAtLong = System.currentTimeMillis();
        this.isPassed = false;
        this.status = "completed";
        this.isPersonalBest = false;
        this.isSynced = false;
    }
    
    @Ignore
    public QuizResult(String quizId, String userId, int totalQuestions, int correctAnswers, 
                     String difficulty, String topic) {
        this();
        this.quizId = quizId;
        this.userId = userId;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = totalQuestions - correctAnswers;
        this.difficulty = difficulty;
        this.topic = topic;
        this.accuracy = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100.0 : 0.0;
        this.score = correctAnswers;
    }
    
    // Helper methods
    public void calculateMetrics() {
        if (totalQuestions > 0) {
            this.accuracy = (double) correctAnswers / totalQuestions * 100.0;
            this.score = correctAnswers;
            this.isPassed = accuracy >= 70.0; // 70% passing threshold
        }
        
        if (timeSpent > 0 && totalQuestions > 0) {
            this.averageTimePerQuestion = (double) timeSpent / totalQuestions / 1000.0; // Convert to seconds
        }
    }
    
    public String getPerformanceLevel() {
        if (accuracy >= 90) return "Excellent";
        if (accuracy >= 80) return "Good";
        if (accuracy >= 70) return "Satisfactory";
        if (accuracy >= 60) return "Needs Improvement";
        return "Poor";
    }
    
    public boolean isHighScore() {
        return accuracy >= 95.0;
    }
    
    public int getCompletionPercentage() {
        if (totalQuestions == 0) return 0;
        return (int) ((double) (correctAnswers + wrongAnswers) / totalQuestions * 100.0);
    }
}

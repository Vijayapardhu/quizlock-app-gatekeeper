package com.smartappgatekeeper.database.dao;

import androidx.room.*;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.Date;

import com.smartappgatekeeper.database.entities.QuizResult;

/**
 * Data Access Object for QuizResult entity
 * Provides database operations for quiz results and performance tracking
 */
@Dao
public interface QuizResultDao {
    
    // Basic CRUD operations
    @Insert
    long insertQuizResult(QuizResult quizResult);
    
    @Insert
    void insertQuizResults(List<QuizResult> quizResults);
    
    @Update
    void updateQuizResult(QuizResult quizResult);
    
    @Delete
    void deleteQuizResult(QuizResult quizResult);
    
    @Query("DELETE FROM quiz_results WHERE id = :id")
    void deleteQuizResultById(int id);
    
    // Query operations
    @Query("SELECT * FROM quiz_results ORDER BY completedAt DESC")
    LiveData<List<QuizResult>> getAllQuizResults();
    
    @Query("SELECT * FROM quiz_results WHERE id = :id")
    LiveData<QuizResult> getQuizResultById(int id);
    
    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY completedAt DESC")
    LiveData<List<QuizResult>> getQuizResultsByUser(String userId);
    
    @Query("SELECT * FROM quiz_results WHERE quizId = :quizId")
    LiveData<List<QuizResult>> getQuizResultsByQuizId(String quizId);
    
    // Performance analytics
    @Query("SELECT * FROM quiz_results WHERE userId = :userId AND topic = :topic ORDER BY completedAt DESC")
    LiveData<List<QuizResult>> getQuizResultsByUserAndTopic(String userId, String topic);
    
    @Query("SELECT * FROM quiz_results WHERE userId = :userId AND difficulty = :difficulty ORDER BY completedAt DESC")
    LiveData<List<QuizResult>> getQuizResultsByUserAndDifficulty(String userId, String difficulty);
    
    @Query("SELECT * FROM quiz_results WHERE userId = :userId AND completedAt >= :startDate AND completedAt <= :endDate ORDER BY completedAt DESC")
    LiveData<List<QuizResult>> getQuizResultsByUserAndDateRange(String userId, Date startDate, Date endDate);
    
    // Statistics queries
    @Query("SELECT COUNT(*) FROM quiz_results WHERE userId = :userId")
    LiveData<Integer> getTotalQuizzesByUser(String userId);
    
    @Query("SELECT AVG(accuracy) FROM quiz_results WHERE userId = :userId")
    LiveData<Double> getAverageAccuracyByUser(String userId);
    
    @Query("SELECT MAX(accuracy) FROM quiz_results WHERE userId = :userId")
    LiveData<Double> getBestAccuracyByUser(String userId);
    
    @Query("SELECT SUM(coinsEarned) FROM quiz_results WHERE userId = :userId")
    LiveData<Integer> getTotalCoinsEarnedByUser(String userId);
    
    @Query("SELECT SUM(experiencePoints) FROM quiz_results WHERE userId = :userId")
    LiveData<Integer> getTotalExperiencePointsByUser(String userId);
    
    // Recent performance
    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY completedAt DESC LIMIT :limit")
    LiveData<List<QuizResult>> getRecentQuizResults(String userId, int limit);
    
    @Query("SELECT * FROM quiz_results WHERE userId = :userId AND isPersonalBest = 1 ORDER BY completedAt DESC")
    LiveData<List<QuizResult>> getPersonalBestResults(String userId);
    
    // Topic and difficulty analysis
    @Query("SELECT topic, AVG(accuracy) as avgAccuracy, COUNT(*) as totalQuizzes FROM quiz_results WHERE userId = :userId GROUP BY topic ORDER BY avgAccuracy DESC")
    LiveData<List<TopicPerformance>> getTopicPerformanceByUser(String userId);
    
    @Query("SELECT difficulty, AVG(accuracy) as avgAccuracy, COUNT(*) as totalQuizzes FROM quiz_results WHERE userId = :userId GROUP BY difficulty ORDER BY avgAccuracy DESC")
    LiveData<List<DifficultyPerformance>> getDifficultyPerformanceByUser(String userId);
    
    // Streak tracking
    @Query("SELECT COUNT(*) FROM quiz_results WHERE userId = :userId AND isPassed = 1 AND completedAt >= :startDate")
    LiveData<Integer> getPassedQuizzesSince(String userId, Date startDate);
    
    // Achievement tracking
    @Query("SELECT COUNT(*) FROM quiz_results WHERE userId = :userId AND accuracy >= :minAccuracy")
    LiveData<Integer> getQuizzesWithMinAccuracy(String userId, double minAccuracy);
    
    @Query("SELECT COUNT(*) FROM quiz_results WHERE userId = :userId AND timeSpent <= :maxTime")
    LiveData<Integer> getQuizzesUnderTime(String userId, long maxTime);
    
    // Cleanup operations
    @Query("DELETE FROM quiz_results WHERE completedAt < :cutoffDate")
    void deleteOldQuizResults(Date cutoffDate);
    
    @Query("DELETE FROM quiz_results WHERE userId = :userId")
    void deleteAllQuizResultsByUser(String userId);
    
    // Data classes for complex queries
    @Entity
    class TopicPerformance {
        public String topic;
        public double avgAccuracy;
        public int totalQuizzes;
    }
    
    @Entity
    class DifficultyPerformance {
        public String difficulty;
        public double avgAccuracy;
        public int totalQuizzes;
    }
}

package com.quizlock.gatekeeper.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.quizlock.gatekeeper.data.model.QuizHistory;

import java.util.List;

/**
 * Data Access Object for QuizHistory entity
 */
@Dao
public interface QuizHistoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizHistory history);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizHistory> histories);
    
    @Update
    void update(QuizHistory history);
    
    @Delete
    void delete(QuizHistory history);
    
    @Query("SELECT * FROM quiz_history ORDER BY answered_at DESC")
    LiveData<List<QuizHistory>> getAllHistory();
    
    @Query("SELECT * FROM quiz_history ORDER BY answered_at DESC LIMIT :limit")
    LiveData<List<QuizHistory>> getRecentHistory(int limit);
    
    @Query("SELECT * FROM quiz_history WHERE category = :category ORDER BY answered_at DESC")
    LiveData<List<QuizHistory>> getHistoryByCategory(String category);
    
    @Query("SELECT * FROM quiz_history WHERE app_package = :appPackage ORDER BY answered_at DESC")
    LiveData<List<QuizHistory>> getHistoryByApp(String appPackage);
    
    @Query("SELECT * FROM quiz_history WHERE is_correct = :isCorrect ORDER BY answered_at DESC")
    LiveData<List<QuizHistory>> getHistoryByCorrectness(boolean isCorrect);
    
    @Query("SELECT COUNT(*) FROM quiz_history")
    LiveData<Integer> getTotalQuestionCount();
    
    @Query("SELECT COUNT(*) FROM quiz_history WHERE is_correct = 1")
    LiveData<Integer> getCorrectAnswerCount();
    
    @Query("SELECT COUNT(*) FROM quiz_history WHERE category = :category")
    int getQuestionCountByCategory(String category);
    
    @Query("SELECT COUNT(*) FROM quiz_history WHERE category = :category AND is_correct = 1")
    int getCorrectAnswerCountByCategory(String category);
    
    @Query("SELECT AVG(time_taken_ms) FROM quiz_history")
    LiveData<Double> getAverageTimePerQuestion();
    
    @Query("SELECT AVG(time_taken_ms) FROM quiz_history WHERE category = :category")
    double getAverageTimeByCategory(String category);
    
    @Query("SELECT * FROM quiz_history WHERE answered_at >= :startDate AND answered_at <= :endDate ORDER BY answered_at DESC")
    LiveData<List<QuizHistory>> getHistoryByDateRange(long startDate, long endDate);
    
    @Query("DELETE FROM quiz_history WHERE answered_at < :timestamp")
    void deleteOldHistory(long timestamp);
}
package com.vijayapardhu.quizlock.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizHistoryDao {
    
    @Query("SELECT * FROM quiz_history ORDER BY timestamp DESC")
    LiveData<List<QuizHistory>> getAllHistory();
    
    @Query("SELECT * FROM quiz_history WHERE packageName = :packageName ORDER BY timestamp DESC")
    LiveData<List<QuizHistory>> getHistoryByPackage(String packageName);
    
    @Query("SELECT * FROM quiz_history WHERE date(timestamp/1000, 'unixepoch') = date('now') ORDER BY timestamp DESC")
    LiveData<List<QuizHistory>> getTodayHistory();
    
    @Query("SELECT COUNT(*) FROM quiz_history WHERE date(timestamp/1000, 'unixepoch') = date('now') AND isCorrect = 1")
    LiveData<Integer> getTodayCorrectAnswers();
    
    @Query("SELECT COUNT(*) FROM quiz_history WHERE date(timestamp/1000, 'unixepoch') = date('now')")
    LiveData<Integer> getTodayTotalQuestions();
    
    @Query("SELECT topic, COUNT(*) as count FROM quiz_history WHERE isCorrect = 1 GROUP BY topic ORDER BY count DESC")
    LiveData<List<TopicStatistic>> getTopicStatistics();
    
    @Query("SELECT COUNT(*) FROM quiz_history WHERE isCorrect = 1")
    LiveData<Integer> getTotalCorrectAnswers();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(QuizHistory history);
    
    @Update
    void updateHistory(QuizHistory history);
    
    @Delete
    void deleteHistory(QuizHistory history);
    
    @Query("DELETE FROM quiz_history WHERE timestamp < :timestamp")
    void deleteOldHistory(long timestamp);
    
    // Inner class for topic statistics
    class TopicStatistic {
        public String topic;
        public int count;
        
        public TopicStatistic(String topic, int count) {
            this.topic = topic;
            this.count = count;
        }
    }
}
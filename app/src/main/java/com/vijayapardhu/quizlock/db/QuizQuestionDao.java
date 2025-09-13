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
public interface QuizQuestionDao {
    
    @Query("SELECT * FROM quiz_questions ORDER BY timestamp DESC")
    LiveData<List<QuizQuestion>> getAllQuestions();
    
    @Query("SELECT * FROM quiz_questions WHERE topic = :topic ORDER BY RANDOM() LIMIT 1")
    QuizQuestion getRandomQuestionByTopic(String topic);
    
    @Query("SELECT * FROM quiz_questions WHERE topic = :topic AND difficulty = :difficulty ORDER BY RANDOM() LIMIT 1")
    QuizQuestion getRandomQuestionByTopicAndDifficulty(String topic, String difficulty);
    
    @Query("SELECT * FROM quiz_questions WHERE isFromAPI = 0")
    List<QuizQuestion> getOfflineQuestions();
    
    @Query("SELECT * FROM quiz_questions WHERE topic = :topic AND isFromAPI = 0 ORDER BY RANDOM() LIMIT 1")
    QuizQuestion getRandomOfflineQuestionByTopic(String topic);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(QuizQuestion question);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<QuizQuestion> questions);
    
    @Update
    void updateQuestion(QuizQuestion question);
    
    @Delete
    void deleteQuestion(QuizQuestion question);
    
    @Query("DELETE FROM quiz_questions WHERE isFromAPI = 1 AND timestamp < :timestamp")
    void deleteOldAPIQuestions(long timestamp);
    
    @Query("SELECT COUNT(*) FROM quiz_questions WHERE topic = :topic")
    int getQuestionCountByTopic(String topic);
    
    @Query("SELECT DISTINCT topic FROM quiz_questions")
    List<String> getAvailableTopics();
}
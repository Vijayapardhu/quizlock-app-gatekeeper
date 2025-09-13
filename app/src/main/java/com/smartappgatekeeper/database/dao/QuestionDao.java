package com.smartappgatekeeper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.smartappgatekeeper.database.entities.Question;
import java.util.List;

/**
 * Data Access Object for Question entity
 * FR-005: System shall display questions from selected topics
 * FR-006: System shall support multiple choice questions (A, B, C, D)
 */
@Dao
public interface QuestionDao {
    
    @Query("SELECT * FROM questions ORDER BY createdAt DESC")
    LiveData<List<Question>> getAllQuestions();
    
    @Query("SELECT * FROM questions WHERE isActive = 1 ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<Question>> getRandomQuestions(int limit);
    
    @Query("SELECT * FROM questions WHERE topic = :topic AND isActive = 1 ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<Question>> getRandomQuestionsByTopic(String topic, int limit);
    
    @Query("SELECT * FROM questions WHERE difficulty = :difficulty AND isActive = 1 ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<Question>> getRandomQuestionsByDifficulty(String difficulty, int limit);
    
    @Query("SELECT * FROM questions WHERE topic = :topic AND difficulty = :difficulty AND isActive = 1 ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<Question>> getRandomQuestionsByTopicAndDifficulty(String topic, String difficulty, int limit);
    
    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    LiveData<Question> getQuestionById(int id);
    
    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    Question getQuestionByIdSync(int id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> questions);
    
    @Update
    void updateQuestion(Question question);
    
    @Delete
    void deleteQuestion(Question question);
    
    @Query("UPDATE questions SET usageCount = usageCount + 1 WHERE id = :questionId")
    void incrementUsageCount(int questionId);
    
    @Query("UPDATE questions SET accuracyRate = :accuracy WHERE id = :questionId")
    void updateAccuracyRate(int questionId, double accuracy);
    
    @Query("SELECT DISTINCT topic FROM questions WHERE isActive = 1 ORDER BY topic ASC")
    LiveData<List<String>> getAllTopics();
    
    @Query("SELECT DISTINCT difficulty FROM questions WHERE isActive = 1 ORDER BY difficulty ASC")
    LiveData<List<String>> getAllDifficulties();
    
    @Query("SELECT COUNT(*) FROM questions WHERE isActive = 1")
    LiveData<Integer> getTotalQuestionsCount();
    
    @Query("SELECT COUNT(*) FROM questions WHERE topic = :topic AND isActive = 1")
    LiveData<Integer> getQuestionsCountByTopic(String topic);
    
    @Query("SELECT COUNT(*) FROM questions WHERE difficulty = :difficulty AND isActive = 1")
    LiveData<Integer> getQuestionsCountByDifficulty(String difficulty);
    
    @Query("SELECT * FROM questions WHERE source = :source AND isActive = 1 ORDER BY createdAt DESC")
    LiveData<List<Question>> getQuestionsBySource(String source);
    
    @Query("SELECT * FROM questions WHERE platformId = :platformId AND isActive = 1 ORDER BY createdAt DESC")
    LiveData<List<Question>> getQuestionsByPlatform(String platformId);
    
    @Query("SELECT * FROM questions WHERE courseId = :courseId AND isActive = 1 ORDER BY moduleId ASC, createdAt ASC")
    LiveData<List<Question>> getQuestionsByCourse(String courseId);
}
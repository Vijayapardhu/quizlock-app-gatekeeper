package com.quizlock.gatekeeper.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.quizlock.gatekeeper.data.model.QuizQuestion;

import java.util.List;

/**
 * Data Access Object for QuizQuestion entity
 */
@Dao
public interface QuizQuestionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizQuestion question);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizQuestion> questions);
    
    @Update
    void update(QuizQuestion question);
    
    @Delete
    void delete(QuizQuestion question);
    
    @Query("SELECT * FROM quiz_questions ORDER BY created_at DESC")
    LiveData<List<QuizQuestion>> getAllQuestions();
    
    @Query("SELECT * FROM quiz_questions WHERE category = :category ORDER BY RANDOM() LIMIT 1")
    QuizQuestion getRandomQuestionByCategory(String category);
    
    @Query("SELECT * FROM quiz_questions WHERE category = :category AND difficulty = :difficulty ORDER BY RANDOM() LIMIT 1")
    QuizQuestion getRandomQuestionByCategoryAndDifficulty(String category, String difficulty);
    
    @Query("SELECT * FROM quiz_questions WHERE id = :id")
    QuizQuestion getQuestionById(int id);
    
    @Query("SELECT * FROM quiz_questions WHERE category = :category")
    LiveData<List<QuizQuestion>> getQuestionsByCategory(String category);
    
    @Query("SELECT * FROM quiz_questions WHERE difficulty = :difficulty")
    LiveData<List<QuizQuestion>> getQuestionsByDifficulty(String difficulty);
    
    @Query("SELECT * FROM quiz_questions WHERE source = :source")
    LiveData<List<QuizQuestion>> getQuestionsBySource(String source);
    
    @Query("SELECT COUNT(*) FROM quiz_questions")
    LiveData<Integer> getQuestionCount();
    
    @Query("SELECT COUNT(*) FROM quiz_questions WHERE category = :category")
    int getQuestionCountByCategory(String category);
    
    @Query("DELETE FROM quiz_questions WHERE source = :source")
    void deleteQuestionsBySource(String source);
    
    @Query("DELETE FROM quiz_questions WHERE created_at < :timestamp")
    void deleteOldQuestions(long timestamp);
    
    @Query("SELECT DISTINCT category FROM quiz_questions ORDER BY category")
    LiveData<List<String>> getAllCategories();
    
    @Query("SELECT DISTINCT difficulty FROM quiz_questions ORDER BY difficulty")
    LiveData<List<String>> getAllDifficulties();
}
package com.quizlock.gatekeeper.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Entity representing quiz attempt history
 */
@Entity(tableName = "quiz_history")
public class QuizHistory {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "question_id")
    private int questionId;
    
    @ColumnInfo(name = "user_answer")
    private String userAnswer;
    
    @ColumnInfo(name = "correct_answer")
    private String correctAnswer;
    
    @ColumnInfo(name = "is_correct")
    private boolean isCorrect;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "difficulty")
    private String difficulty;
    
    @ColumnInfo(name = "app_package")
    private String appPackage; // Which app was being unlocked
    
    @ColumnInfo(name = "time_taken_ms")
    private long timeTakenMs; // Time taken to answer in milliseconds
    
    @ColumnInfo(name = "answered_at")
    private long answeredAt;
    
    // Constructors
    public QuizHistory() {
        this.answeredAt = System.currentTimeMillis();
    }
    
    public QuizHistory(int questionId, String userAnswer, String correctAnswer, 
                      boolean isCorrect, String category, String difficulty, 
                      String appPackage, long timeTakenMs) {
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
        this.category = category;
        this.difficulty = difficulty;
        this.appPackage = appPackage;
        this.timeTakenMs = timeTakenMs;
        this.answeredAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getAppPackage() { return appPackage; }
    public void setAppPackage(String appPackage) { this.appPackage = appPackage; }
    
    public long getTimeTakenMs() { return timeTakenMs; }
    public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
    
    public long getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(long answeredAt) { this.answeredAt = answeredAt; }
}
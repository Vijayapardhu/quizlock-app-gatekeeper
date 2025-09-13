package com.quizlock.gatekeeper.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Entity representing a quiz question
 */
@Entity(tableName = "quiz_questions")
public class QuizQuestion {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "question_text")
    private String questionText;
    
    @ColumnInfo(name = "correct_answer")
    private String correctAnswer;
    
    @ColumnInfo(name = "option_a")
    private String optionA;
    
    @ColumnInfo(name = "option_b")
    private String optionB;
    
    @ColumnInfo(name = "option_c")
    private String optionC;
    
    @ColumnInfo(name = "option_d")
    private String optionD;
    
    @ColumnInfo(name = "category")
    private String category; // "math", "coding", "general"
    
    @ColumnInfo(name = "difficulty")
    private String difficulty; // "easy", "medium", "hard"
    
    @ColumnInfo(name = "source")
    private String source; // "gemini", "local"
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    // Constructors
    public QuizQuestion() {
        this.createdAt = System.currentTimeMillis();
    }
    
    public QuizQuestion(String questionText, String correctAnswer, String optionA, 
                       String optionB, String optionC, String optionD, 
                       String category, String difficulty, String source) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.category = category;
        this.difficulty = difficulty;
        this.source = source;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    // Helper methods
    public String[] getAllOptions() {
        return new String[]{optionA, optionB, optionC, optionD};
    }
    
    public boolean isCorrectAnswer(String answer) {
        return correctAnswer != null && correctAnswer.equalsIgnoreCase(answer.trim());
    }
}
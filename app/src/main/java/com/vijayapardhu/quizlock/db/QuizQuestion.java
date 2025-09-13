package com.vijayapardhu.quizlock.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_questions")
public class QuizQuestion {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String question;
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public String correctAnswer;
    public String topic;
    public String difficulty;
    public boolean isFromAPI;
    public long timestamp;
    
    public QuizQuestion() {}
    
    public QuizQuestion(String question, String optionA, String optionB, String optionC, 
                       String optionD, String correctAnswer, String topic, String difficulty, 
                       boolean isFromAPI) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.topic = topic;
        this.difficulty = difficulty;
        this.isFromAPI = isFromAPI;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public boolean isFromAPI() { return isFromAPI; }
    public void setFromAPI(boolean fromAPI) { isFromAPI = fromAPI; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
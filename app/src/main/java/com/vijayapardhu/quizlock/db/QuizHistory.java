package com.vijayapardhu.quizlock.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_history")
public class QuizHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String packageName;
    public String question;
    public String userAnswer;
    public String correctAnswer;
    public boolean isCorrect;
    public String topic;
    public long timestamp;
    public int timeTakenSeconds;
    
    public QuizHistory() {}
    
    public QuizHistory(String packageName, String question, String userAnswer, 
                      String correctAnswer, boolean isCorrect, String topic, int timeTakenSeconds) {
        this.packageName = packageName;
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
        this.topic = topic;
        this.timeTakenSeconds = timeTakenSeconds;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }
}
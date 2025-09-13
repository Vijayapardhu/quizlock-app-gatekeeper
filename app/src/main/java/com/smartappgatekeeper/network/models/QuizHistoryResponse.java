package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class QuizHistoryResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("score")
    private int score;
    
    @SerializedName("total_questions")
    private int totalQuestions;
    
    @SerializedName("topic")
    private String topic;
    
    @SerializedName("difficulty")
    private String difficulty;
    
    @SerializedName("time_spent")
    private long timeSpent;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public long getTimeSpent() { return timeSpent; }
    public void setTimeSpent(long timeSpent) { this.timeSpent = timeSpent; }
}

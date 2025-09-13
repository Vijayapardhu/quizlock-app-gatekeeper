package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class QuizResultResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("score")
    private int score;
    
    @SerializedName("total_questions")
    private int totalQuestions;
    
    @SerializedName("correct_answers")
    private int correctAnswers;
    
    @SerializedName("coins_earned")
    private int coinsEarned;
    
    @SerializedName("experience_gained")
    private int experienceGained;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public int getCoinsEarned() { return coinsEarned; }
    public void setCoinsEarned(int coinsEarned) { this.coinsEarned = coinsEarned; }
    
    public int getExperienceGained() { return experienceGained; }
    public void setExperienceGained(int experienceGained) { this.experienceGained = experienceGained; }
}

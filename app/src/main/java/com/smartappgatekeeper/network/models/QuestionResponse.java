package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuestionResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("question")
    private String question;
    
    @SerializedName("options")
    private List<String> options;
    
    @SerializedName("correct_answer")
    private int correctAnswer;
    
    @SerializedName("explanation")
    private String explanation;
    
    @SerializedName("topic")
    private String topic;
    
    @SerializedName("difficulty")
    private String difficulty;
    
    @SerializedName("points")
    private int points;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    
    public int getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}

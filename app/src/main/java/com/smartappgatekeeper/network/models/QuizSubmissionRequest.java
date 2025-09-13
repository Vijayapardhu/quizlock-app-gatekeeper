package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizSubmissionRequest {
    @SerializedName("question_id")
    private String questionId;
    
    @SerializedName("selected_answer")
    private int selectedAnswer;
    
    @SerializedName("time_spent")
    private long timeSpent;
    
    @SerializedName("quiz_session_id")
    private String quizSessionId;

    // Constructors
    public QuizSubmissionRequest() {}

    public QuizSubmissionRequest(String questionId, int selectedAnswer, long timeSpent, String quizSessionId) {
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
        this.timeSpent = timeSpent;
        this.quizSessionId = quizSessionId;
    }

    // Getters and Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }
    
    public int getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(int selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public long getTimeSpent() { return timeSpent; }
    public void setTimeSpent(long timeSpent) { this.timeSpent = timeSpent; }
    
    public String getQuizSessionId() { return quizSessionId; }
    public void setQuizSessionId(String quizSessionId) { this.quizSessionId = quizSessionId; }
}

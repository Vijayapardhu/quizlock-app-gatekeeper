package com.smartappgatekeeper.network.dto;

import java.util.List;

/**
 * Quiz submission request DTO
 */
public class QuizSubmissionRequest {
    private String quizId;
    private List<AnswerSubmission> answers;
    private int timeSpent; // in seconds
    private String difficulty;
    private long timestamp;
    
    public QuizSubmissionRequest() {}
    
    public QuizSubmissionRequest(String quizId, List<AnswerSubmission> answers, int timeSpent, 
                               String difficulty, long timestamp) {
        this.quizId = quizId;
        this.answers = answers;
        this.timeSpent = timeSpent;
        this.difficulty = difficulty;
        this.timestamp = timestamp;
    }
    
    // Getters and setters
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    
    public List<AnswerSubmission> getAnswers() { return answers; }
    public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }
    
    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    /**
     * Answer submission inner class
     */
    public static class AnswerSubmission {
        private String questionId;
        private String selectedAnswer;
        private boolean isCorrect;
        private int timeSpent; // in seconds
        
        public AnswerSubmission() {}
        
        public AnswerSubmission(String questionId, String selectedAnswer, boolean isCorrect, int timeSpent) {
            this.questionId = questionId;
            this.selectedAnswer = selectedAnswer;
            this.isCorrect = isCorrect;
            this.timeSpent = timeSpent;
        }
        
        // Getters and setters
        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }
        
        public String getSelectedAnswer() { return selectedAnswer; }
        public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }
        
        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }
        
        public int getTimeSpent() { return timeSpent; }
        public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    }
}

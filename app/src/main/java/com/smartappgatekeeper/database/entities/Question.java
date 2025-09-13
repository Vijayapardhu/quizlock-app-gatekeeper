package com.smartappgatekeeper.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entity representing a quiz question
 * FR-005: System shall display questions from selected topics
 * FR-006: System shall support multiple choice questions (A, B, C, D)
 * FR-009: System shall generate questions using AI (Gemini) when available
 */
@Entity(tableName = "questions")
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String questionText;
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public String correctAnswer; // "A", "B", "C", or "D"
    public String explanation;
    public String topic;
    public String difficulty; // "easy", "medium", "hard"
    public int timeLimitSeconds;
    public boolean isActive;
    public Date createdAt;
    public Date lastUpdated;
    
    // AI-generated question metadata
    public String source; // "ai_generated", "manual", "imported"
    public String aiModel; // "gemini", "gpt", etc.
    public int usageCount;
    public double accuracyRate; // Percentage of correct answers
    
    // Learning platform integration
    public String platformId; // ID from learning platform
    public String courseId; // Course this question belongs to
    public int moduleId; // Module within course
    
    public Question() {
        this.createdAt = new Date();
        this.lastUpdated = new Date();
        this.isActive = true;
        this.timeLimitSeconds = 30;
        this.source = "manual";
        this.usageCount = 0;
        this.accuracyRate = 0.0;
    }

    // Constructor with parameters for compatibility
    @Ignore
    public Question(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this();
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getQuestion() { return questionText; }
    public void setQuestion(String questionText) { this.questionText = questionText; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
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
    
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public int getTimeLimitSeconds() { return timeLimitSeconds; }
    public void setTimeLimitSeconds(int timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }
    
    public int getUsageCount() { return usageCount; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    
    public double getAccuracyRate() { return accuracyRate; }
    public void setAccuracyRate(double accuracyRate) { this.accuracyRate = accuracyRate; }
    
    public String getPlatformId() { return platformId; }
    public void setPlatformId(String platformId) { this.platformId = platformId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }
}

package com.smartappgatekeeper.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entity representing app usage events and quiz attempts
 * FR-004: System shall log all interception events for analytics
 * FR-008: System shall track quiz performance and accuracy
 */
@Entity(tableName = "usage_events")
public class UsageEvent {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String packageName;
    public String appName;
    public String eventType; // "app_launch", "quiz_attempt", "quiz_success", "quiz_failure", "app_unlock", "app_lock"
    public Date timestamp;
    public long timestampLong; // Long timestamp for compatibility
    public long durationSeconds;
    public boolean success;
    public String details; // JSON string with additional event data
    
    // Quiz-specific fields
    public int questionId;
    public String selectedAnswer;
    public String correctAnswer;
    public int timeSpentSeconds;
    public int coinsEarned;
    
    // Session tracking
    public String sessionId;
    public int attemptNumber;
    
    // Learning platform integration
    public String platformId;
    public String courseId;
    public int moduleId;
    
    public UsageEvent() {
        this.timestamp = new Date();
        this.timestampLong = System.currentTimeMillis();
        this.success = false;
        this.durationSeconds = 0;
        this.timeSpentSeconds = 0;
        this.coinsEarned = 0;
        this.attemptNumber = 1;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { 
        this.timestamp = timestamp; 
        this.timestampLong = timestamp.getTime();
    }
    
    public long getTimestampLong() { return timestampLong; }
    public void setTimestampLong(long timestampLong) { 
        this.timestampLong = timestampLong; 
        this.timestamp = new Date(timestampLong);
    }
    
    // Overloaded setTimestamp for long compatibility
    public void setTimestamp(long timestampLong) {
        this.timestampLong = timestampLong;
        this.timestamp = new Date(timestampLong);
    }
    
    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    
    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public int getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(int timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }
    
    public int getCoinsEarned() { return coinsEarned; }
    public void setCoinsEarned(int coinsEarned) { this.coinsEarned = coinsEarned; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public int getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; }
    
    public String getPlatformId() { return platformId; }
    public void setPlatformId(String platformId) { this.platformId = platformId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }
}
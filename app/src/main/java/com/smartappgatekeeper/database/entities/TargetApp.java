package com.smartappgatekeeper.database.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entity representing a target app that will be gated behind quizzes
 * FR-001: System shall intercept all configured target apps
 * FR-014: System shall support configurable time limits per app
 */
@Entity(tableName = "target_apps")
public class TargetApp implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String packageName;
    public String appName;
    public String iconPath;
    public boolean isEnabled;
    public Date createdAt;
    public Date lastUpdated;
    
    // Additional fields for compatibility
    public String category;
    public boolean selected;
    
    // Time limits (FR-014, FR-015)
    public int dailyLimitMinutes;
    public int perUnlockDurationMinutes;
    public int maxUsesPerDay;
    public int currentUsesToday;
    
    // Quiz settings for this specific app
    public int questionsPerUnlock;
    public int timePerQuestionSeconds;
    public int wrongAnswerDelayMinutes;
    public String difficultyLevel; // "easy", "medium", "hard"
    public String selectedTopics; // JSON string of topics
    
    // Emergency bypass settings
    public boolean emergencyBypassEnabled;
    public int emergencyUsesPerDay;
    public int emergencyUsesUsed;
    
    public TargetApp() {
        this.createdAt = new Date();
        this.lastUpdated = new Date();
        this.isEnabled = true;
        this.dailyLimitMinutes = 60; // 1 hour default
        this.perUnlockDurationMinutes = 10; // 10 minutes default
        this.maxUsesPerDay = 10;
        this.questionsPerUnlock = 1;
        this.timePerQuestionSeconds = 30;
        this.wrongAnswerDelayMinutes = 5;
        this.difficultyLevel = "easy";
        this.emergencyBypassEnabled = false;
        this.emergencyUsesPerDay = 3;
        this.currentUsesToday = 0;
        this.emergencyUsesUsed = 0;
        this.selected = false;
    }

    // Constructor with parameters for compatibility
    @Ignore
    public TargetApp(String packageName, String appName, String category, boolean isEnabled) {
        this();
        this.packageName = packageName;
        this.appName = appName;
        this.category = category;
        this.isEnabled = isEnabled;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    
    public String getIconPath() { return iconPath; }
    public void setIconPath(String iconPath) { this.iconPath = iconPath; }
    
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    
    public int getDailyLimitMinutes() { return dailyLimitMinutes; }
    public void setDailyLimitMinutes(int dailyLimitMinutes) { this.dailyLimitMinutes = dailyLimitMinutes; }
    
    public int getPerUnlockDurationMinutes() { return perUnlockDurationMinutes; }
    public void setPerUnlockDurationMinutes(int perUnlockDurationMinutes) { this.perUnlockDurationMinutes = perUnlockDurationMinutes; }
    
    public int getMaxUsesPerDay() { return maxUsesPerDay; }
    public void setMaxUsesPerDay(int maxUsesPerDay) { this.maxUsesPerDay = maxUsesPerDay; }
    
    public int getCurrentUsesToday() { return currentUsesToday; }
    public void setCurrentUsesToday(int currentUsesToday) { this.currentUsesToday = currentUsesToday; }
    
    public int getQuestionsPerUnlock() { return questionsPerUnlock; }
    public void setQuestionsPerUnlock(int questionsPerUnlock) { this.questionsPerUnlock = questionsPerUnlock; }
    
    public int getTimePerQuestionSeconds() { return timePerQuestionSeconds; }
    public void setTimePerQuestionSeconds(int timePerQuestionSeconds) { this.timePerQuestionSeconds = timePerQuestionSeconds; }
    
    public int getWrongAnswerDelayMinutes() { return wrongAnswerDelayMinutes; }
    public void setWrongAnswerDelayMinutes(int wrongAnswerDelayMinutes) { this.wrongAnswerDelayMinutes = wrongAnswerDelayMinutes; }
    
    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public String getSelectedTopics() { return selectedTopics; }
    public void setSelectedTopics(String selectedTopics) { this.selectedTopics = selectedTopics; }
    
    public boolean isEmergencyBypassEnabled() { return emergencyBypassEnabled; }
    public void setEmergencyBypassEnabled(boolean emergencyBypassEnabled) { this.emergencyBypassEnabled = emergencyBypassEnabled; }
    
    public int getEmergencyUsesPerDay() { return emergencyUsesPerDay; }
    public void setEmergencyUsesPerDay(int emergencyUsesPerDay) { this.emergencyUsesPerDay = emergencyUsesPerDay; }
    
    public int getEmergencyUsesUsed() { return emergencyUsesUsed; }
    public void setEmergencyUsesUsed(int emergencyUsesUsed) { this.emergencyUsesUsed = emergencyUsesUsed; }
    
    // Parcelable implementation
    @Ignore
    protected TargetApp(Parcel in) {
        id = in.readInt();
        packageName = in.readString();
        appName = in.readString();
        iconPath = in.readString();
        isEnabled = in.readByte() != 0;
        category = in.readString();
        selected = in.readByte() != 0;
        dailyLimitMinutes = in.readInt();
        perUnlockDurationMinutes = in.readInt();
        maxUsesPerDay = in.readInt();
        currentUsesToday = in.readInt();
        questionsPerUnlock = in.readInt();
        timePerQuestionSeconds = in.readInt();
        wrongAnswerDelayMinutes = in.readInt();
        difficultyLevel = in.readString();
        selectedTopics = in.readString();
        emergencyBypassEnabled = in.readByte() != 0;
        emergencyUsesPerDay = in.readInt();
        emergencyUsesUsed = in.readInt();
    }
    
    public static final Creator<TargetApp> CREATOR = new Creator<TargetApp>() {
        @Override
        public TargetApp createFromParcel(Parcel in) {
            return new TargetApp(in);
        }
        
        @Override
        public TargetApp[] newArray(int size) {
            return new TargetApp[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(packageName);
        dest.writeString(appName);
        dest.writeString(iconPath);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeString(category);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeInt(dailyLimitMinutes);
        dest.writeInt(perUnlockDurationMinutes);
        dest.writeInt(maxUsesPerDay);
        dest.writeInt(currentUsesToday);
        dest.writeInt(questionsPerUnlock);
        dest.writeInt(timePerQuestionSeconds);
        dest.writeInt(wrongAnswerDelayMinutes);
        dest.writeString(difficultyLevel);
        dest.writeString(selectedTopics);
        dest.writeByte((byte) (emergencyBypassEnabled ? 1 : 0));
        dest.writeInt(emergencyUsesPerDay);
        dest.writeInt(emergencyUsesUsed);
    }
}

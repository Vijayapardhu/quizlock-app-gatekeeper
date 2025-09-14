package com.smartappgatekeeper.model;

/**
 * Achievement model class
 * Represents an achievement in the app
 */
public class Achievement {
    private String id;
    private String title;
    private String description;
    private String details;
    private int iconResId;
    private int targetValue;
    private int currentValue;
    private boolean isCompleted;
    private boolean isLocked;
    
    public Achievement(String id, String title, String description, String details, 
                      int iconResId, int targetValue, int currentValue, 
                      boolean isCompleted, boolean isLocked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.details = details;
        this.iconResId = iconResId;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.isCompleted = isCompleted;
        this.isLocked = isLocked;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public int getIconResId() {
        return iconResId;
    }
    
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
    
    public int getTargetValue() {
        return targetValue;
    }
    
    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }
    
    public int getCurrentValue() {
        return currentValue;
    }
    
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    public boolean isLocked() {
        return isLocked;
    }
    
    public void setLocked(boolean locked) {
        isLocked = locked;
    }
    
    public int getProgressPercentage() {
        if (targetValue == 0) return 0;
        return Math.min(100, (currentValue * 100) / targetValue);
    }
}

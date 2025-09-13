package com.vijayapardhu.quizlock.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_preferences")
public class UserPreferences {
    @PrimaryKey
    public String key;
    
    public String value;
    public long lastModified;
    
    public UserPreferences() {}
    
    public UserPreferences(String key, String value) {
        this.key = key;
        this.value = value;
        this.lastModified = System.currentTimeMillis();
    }
    
    // Getters and setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public String getValue() { return value; }
    public void setValue(String value) { 
        this.value = value; 
        this.lastModified = System.currentTimeMillis();
    }
    
    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }
}
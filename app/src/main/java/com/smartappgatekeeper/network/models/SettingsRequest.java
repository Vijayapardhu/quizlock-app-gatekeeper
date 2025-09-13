package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class SettingsRequest {
    @SerializedName("notifications_enabled")
    private boolean notificationsEnabled;
    
    @SerializedName("sound_enabled")
    private boolean soundEnabled;
    
    @SerializedName("vibration_enabled")
    private boolean vibrationEnabled;
    
    @SerializedName("dark_mode")
    private boolean darkMode;
    
    @SerializedName("language")
    private String language;

    // Constructors
    public SettingsRequest() {}

    public SettingsRequest(boolean notificationsEnabled, boolean soundEnabled, boolean vibrationEnabled, 
                          boolean darkMode, String language) {
        this.notificationsEnabled = notificationsEnabled;
        this.soundEnabled = soundEnabled;
        this.vibrationEnabled = vibrationEnabled;
        this.darkMode = darkMode;
        this.language = language;
    }

    // Getters and Setters
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }
    
    public boolean isVibrationEnabled() { return vibrationEnabled; }
    public void setVibrationEnabled(boolean vibrationEnabled) { this.vibrationEnabled = vibrationEnabled; }
    
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}

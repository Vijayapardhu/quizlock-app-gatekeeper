package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class SettingsResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("settings")
    private SettingsData settings;

    public static class SettingsData {
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

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public SettingsData getSettings() { return settings; }
    public void setSettings(SettingsData settings) { this.settings = settings; }
}

package com.quizlock.gatekeeper.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages shared preferences for the application
 */
public class PreferencesManager {
    
    private static final String PREFS_NAME = "quizlock_prefs";
    
    // Preference keys
    private static final String KEY_FIRST_RUN = "first_run";
    private static final String KEY_ACCESSIBILITY_ENABLED = "accessibility_enabled";
    private static final String KEY_QUIZ_DIFFICULTY = "quiz_difficulty";
    private static final String KEY_UNLOCK_DURATION = "unlock_duration";
    private static final String KEY_PREFERRED_CATEGORY = "preferred_category";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_GEMINI_API_KEY = "gemini_api_key";
    private static final String KEY_OFFLINE_MODE = "offline_mode";
    private static final String KEY_DAILY_GOAL = "daily_goal";
    private static final String KEY_THEME_MODE = "theme_mode";
    
    // Default values
    public static final String DEFAULT_DIFFICULTY = "medium";
    public static final long DEFAULT_UNLOCK_DURATION = 15 * 60 * 1000; // 15 minutes
    public static final String DEFAULT_CATEGORY = "general";
    public static final int DEFAULT_DAILY_GOAL = 10;
    public static final String DEFAULT_THEME = "system";
    
    private final SharedPreferences preferences;
    
    public PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    // First run management
    public boolean isFirstRun() {
        return preferences.getBoolean(KEY_FIRST_RUN, true);
    }
    
    public void setFirstRunCompleted() {
        preferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
    }
    
    // Accessibility service
    public boolean isAccessibilityEnabled() {
        return preferences.getBoolean(KEY_ACCESSIBILITY_ENABLED, false);
    }
    
    public void setAccessibilityEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_ACCESSIBILITY_ENABLED, enabled).apply();
    }
    
    // Quiz settings
    public String getQuizDifficulty() {
        return preferences.getString(KEY_QUIZ_DIFFICULTY, DEFAULT_DIFFICULTY);
    }
    
    public void setQuizDifficulty(String difficulty) {
        preferences.edit().putString(KEY_QUIZ_DIFFICULTY, difficulty).apply();
    }
    
    public long getUnlockDuration() {
        return preferences.getLong(KEY_UNLOCK_DURATION, DEFAULT_UNLOCK_DURATION);
    }
    
    public void setUnlockDuration(long duration) {
        preferences.edit().putLong(KEY_UNLOCK_DURATION, duration).apply();
    }
    
    public String getPreferredCategory() {
        return preferences.getString(KEY_PREFERRED_CATEGORY, DEFAULT_CATEGORY);
    }
    
    public void setPreferredCategory(String category) {
        preferences.edit().putString(KEY_PREFERRED_CATEGORY, category).apply();
    }
    
    // Notifications
    public boolean areNotificationsEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }
    
    public void setNotificationsEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }
    
    public boolean isSoundEnabled() {
        return preferences.getBoolean(KEY_SOUND_ENABLED, true);
    }
    
    public void setSoundEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }
    
    public boolean isVibrationEnabled() {
        return preferences.getBoolean(KEY_VIBRATION_ENABLED, true);
    }
    
    public void setVibrationEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply();
    }
    
    // API settings
    public String getGeminiApiKey() {
        return preferences.getString(KEY_GEMINI_API_KEY, "");
    }
    
    public void setGeminiApiKey(String apiKey) {
        preferences.edit().putString(KEY_GEMINI_API_KEY, apiKey).apply();
    }
    
    public boolean isOfflineMode() {
        return preferences.getBoolean(KEY_OFFLINE_MODE, false);
    }
    
    public void setOfflineMode(boolean offline) {
        preferences.edit().putBoolean(KEY_OFFLINE_MODE, offline).apply();
    }
    
    // Goals and progress
    public int getDailyGoal() {
        return preferences.getInt(KEY_DAILY_GOAL, DEFAULT_DAILY_GOAL);
    }
    
    public void setDailyGoal(int goal) {
        preferences.edit().putInt(KEY_DAILY_GOAL, goal).apply();
    }
    
    // Theme
    public String getThemeMode() {
        return preferences.getString(KEY_THEME_MODE, DEFAULT_THEME);
    }
    
    public void setThemeMode(String theme) {
        preferences.edit().putString(KEY_THEME_MODE, theme).apply();
    }
    
    // Clear all preferences (for reset functionality)
    public void clearAllPreferences() {
        preferences.edit().clear().apply();
    }
    
    // Export preferences for backup
    public SharedPreferences getSharedPreferences() {
        return preferences;
    }
}
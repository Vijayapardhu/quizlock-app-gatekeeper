package com.smartappgatekeeper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Helper class for managing SharedPreferences
 * Provides easy access to app preferences and settings
 */
public class SharedPreferencesHelper {
    
    private static SharedPreferencesHelper instance;
    private SharedPreferences preferences;
    
    private SharedPreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    // Boolean preferences
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
    
    public void setBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }
    
    // String preferences
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }
    
    public void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
    
    // Integer preferences
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }
    
    public void setInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }
    
    // Long preferences
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }
    
    public void setLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }
    
    // Float preferences
    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }
    
    public void setFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }
    
    // Specific preference getters
    public boolean isFirstLaunch() {
        return getBoolean(Constants.PREF_FIRST_LAUNCH, true);
    }
    
    public void setFirstLaunch(boolean isFirstLaunch) {
        setBoolean(Constants.PREF_FIRST_LAUNCH, isFirstLaunch);
    }
    
    public boolean isOnboardingComplete() {
        return getBoolean(Constants.PREF_ONBOARDING_COMPLETE, false);
    }
    
    public void setOnboardingComplete(boolean isComplete) {
        setBoolean(Constants.PREF_ONBOARDING_COMPLETE, isComplete);
    }
    
    public String getUserId() {
        return getString(Constants.PREF_USER_ID, "");
    }
    
    public void setUserId(String userId) {
        setString(Constants.PREF_USER_ID, userId);
    }
    
    public int getCurrentStreak() {
        return getInt(Constants.PREF_CURRENT_STREAK, 0);
    }
    
    public void setCurrentStreak(int streak) {
        setInt(Constants.PREF_CURRENT_STREAK, streak);
    }
    
    public int getTotalCoins() {
        return getInt(Constants.PREF_TOTAL_COINS, 0);
    }
    
    public void setTotalCoins(int coins) {
        setInt(Constants.PREF_TOTAL_COINS, coins);
    }
    
    public void addCoins(int coins) {
        int currentCoins = getTotalCoins();
        setTotalCoins(currentCoins + coins);
    }
    
    public boolean spendCoins(int coins) {
        int currentCoins = getTotalCoins();
        if (currentCoins >= coins) {
            setTotalCoins(currentCoins - coins);
            return true;
        }
        return false;
    }
    
    public long getLastQuizDate() {
        return getLong(Constants.PREF_LAST_QUIZ_DATE, 0);
    }
    
    public void setLastQuizDate(long timestamp) {
        setLong(Constants.PREF_LAST_QUIZ_DATE, timestamp);
    }
    
    public boolean isNotificationEnabled() {
        return getBoolean(Constants.PREF_NOTIFICATION_ENABLED, true);
    }
    
    public void setNotificationEnabled(boolean enabled) {
        setBoolean(Constants.PREF_NOTIFICATION_ENABLED, enabled);
    }
    
    public boolean isSoundEnabled() {
        return getBoolean(Constants.PREF_SOUND_ENABLED, true);
    }
    
    public void setSoundEnabled(boolean enabled) {
        setBoolean(Constants.PREF_SOUND_ENABLED, enabled);
    }
    
    public boolean isVibrationEnabled() {
        return getBoolean(Constants.PREF_VIBRATION_ENABLED, true);
    }
    
    public void setVibrationEnabled(boolean enabled) {
        setBoolean(Constants.PREF_VIBRATION_ENABLED, enabled);
    }
    
    public boolean isDarkModeEnabled() {
        return getBoolean(Constants.PREF_DARK_MODE, false);
    }
    
    public void setDarkModeEnabled(boolean enabled) {
        setBoolean(Constants.PREF_DARK_MODE, enabled);
    }
    
    public boolean isAnalyticsEnabled() {
        return getBoolean(Constants.PREF_ANALYTICS_ENABLED, true);
    }
    
    public void setAnalyticsEnabled(boolean enabled) {
        setBoolean(Constants.PREF_ANALYTICS_ENABLED, enabled);
    }
    
    // Quiz preferences
    public int getQuizDifficulty() {
        return getInt("quiz_difficulty", Constants.DIFFICULTY_EASY);
    }
    
    public void setQuizDifficulty(int difficulty) {
        setInt("quiz_difficulty", difficulty);
    }
    
    public int getSessionDuration() {
        return getInt("session_duration", Constants.SESSION_5_MIN);
    }
    
    public void setSessionDuration(int duration) {
        setInt("session_duration", duration);
    }
    
    public String getSelectedLanguage() {
        return getString("selected_language", Constants.LANGUAGE_ENGLISH);
    }
    
    public void setSelectedLanguage(String language) {
        setString("selected_language", language);
    }
    
    public String getSelectedGoal() {
        return getString("selected_goal", Constants.GOAL_REDUCE_SCREEN_TIME);
    }
    
    public void setSelectedGoal(String goal) {
        setString("selected_goal", goal);
    }
    
    // Achievement preferences
    public boolean isAchievementUnlocked(String achievementId) {
        return getBoolean("achievement_" + achievementId, false);
    }
    
    public void setAchievementUnlocked(String achievementId, boolean unlocked) {
        setBoolean("achievement_" + achievementId, unlocked);
    }
    
    // Store preferences
    public boolean isItemPurchased(String itemId) {
        return getBoolean("purchased_" + itemId, false);
    }
    
    public void setItemPurchased(String itemId, boolean purchased) {
        setBoolean("purchased_" + itemId, purchased);
    }
    
    // Streak saver preferences
    public int getStreakSaverUsesToday() {
        long today = System.currentTimeMillis() / (24 * 60 * 60 * 1000);
        long lastUse = getLong("streak_saver_last_use", 0);
        
        if (today > lastUse) {
            setInt("streak_saver_uses_today", 0);
            setLong("streak_saver_last_use", today);
        }
        
        return getInt("streak_saver_uses_today", 0);
    }
    
    public boolean canUseStreakSaver() {
        return getStreakSaverUsesToday() < Constants.MAX_STREAK_SAVER_USES;
    }
    
    public void useStreakSaver() {
        int uses = getStreakSaverUsesToday();
        setInt("streak_saver_uses_today", uses + 1);
    }
    
    // Statistics preferences
    public int getTotalQuizzesCompleted() {
        return getInt("total_quizzes_completed", 0);
    }
    
    public void setTotalQuizzesCompleted(int count) {
        setInt("total_quizzes_completed", count);
    }
    
    public void incrementTotalQuizzesCompleted() {
        int count = getTotalQuizzesCompleted();
        setTotalQuizzesCompleted(count + 1);
    }
    
    public int getTotalQuestionsAnswered() {
        return getInt("total_questions_answered", 0);
    }
    
    public void setTotalQuestionsAnswered(int count) {
        setInt("total_questions_answered", count);
    }
    
    public void addQuestionsAnswered(int count) {
        int total = getTotalQuestionsAnswered();
        setTotalQuestionsAnswered(total + count);
    }
    
    public int getTotalTimeSaved() {
        return getInt("total_time_saved", 0);
    }
    
    public void setTotalTimeSaved(int minutes) {
        setInt("total_time_saved", minutes);
    }
    
    public void addTimeSaved(int minutes) {
        int total = getTotalTimeSaved();
        setTotalTimeSaved(total + minutes);
    }
    
    public float getAverageAccuracy() {
        return getFloat("average_accuracy", 0.0f);
    }
    
    public void setAverageAccuracy(float accuracy) {
        setFloat("average_accuracy", accuracy);
    }
    
    public void updateAverageAccuracy(int correctAnswers, int totalAnswers) {
        if (totalAnswers > 0) {
            float currentAccuracy = getAverageAccuracy();
            int totalQuestions = getTotalQuestionsAnswered();
            
            if (totalQuestions > 0) {
                float newAccuracy = ((currentAccuracy * totalQuestions) + correctAnswers) / (totalQuestions + totalAnswers);
                setAverageAccuracy(newAccuracy);
            } else {
                setAverageAccuracy((float) correctAnswers / totalAnswers * 100);
            }
        }
    }
    
    // Utility methods
    public void clearAll() {
        preferences.edit().clear().apply();
    }
    
    public void clearUserData() {
        // Clear user-specific data but keep app settings
        preferences.edit()
            .remove(Constants.PREF_USER_ID)
            .remove(Constants.PREF_CURRENT_STREAK)
            .remove(Constants.PREF_TOTAL_COINS)
            .remove(Constants.PREF_LAST_QUIZ_DATE)
            .remove("total_quizzes_completed")
            .remove("total_questions_answered")
            .remove("total_time_saved")
            .remove("average_accuracy")
            .apply();
    }
    
    public boolean contains(String key) {
        return preferences.contains(key);
    }
    
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }
}

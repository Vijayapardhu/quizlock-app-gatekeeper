package com.vijayapardhu.quizlock.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserPreferencesDao {
    
    @Query("SELECT * FROM user_preferences")
    LiveData<List<UserPreferences>> getAllPreferences();
    
    @Query("SELECT value FROM user_preferences WHERE key = :key")
    String getPreference(String key);
    
    @Query("SELECT value FROM user_preferences WHERE key = :key")
    LiveData<String> getPreferenceLive(String key);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreference(UserPreferences preference);
    
    @Update
    void updatePreference(UserPreferences preference);
    
    @Delete
    void deletePreference(UserPreferences preference);
    
    @Query("DELETE FROM user_preferences WHERE key = :key")
    void deletePreferenceByKey(String key);
    
    // Convenience methods for common preferences
    @Query("SELECT value FROM user_preferences WHERE key = 'selected_topics'")
    String getSelectedTopics();
    
    @Query("SELECT value FROM user_preferences WHERE key = 'onboarding_completed'")
    String getOnboardingCompleted();
    
    @Query("SELECT value FROM user_preferences WHERE key = 'accessibility_enabled'")
    String getAccessibilityEnabled();
    
    @Query("SELECT value FROM user_preferences WHERE key = 'current_streak'")
    String getCurrentStreak();
    
    @Query("SELECT value FROM user_preferences WHERE key = 'best_streak'")
    String getBestStreak();
}
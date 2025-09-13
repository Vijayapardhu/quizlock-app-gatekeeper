package com.smartappgatekeeper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.smartappgatekeeper.database.entities.TargetApp;
import java.util.List;

/**
 * Data Access Object for TargetApp entity
 * FR-001: System shall intercept all configured target apps
 */
@Dao
public interface TargetAppDao {
    
    @Query("SELECT * FROM target_apps ORDER BY appName ASC")
    LiveData<List<TargetApp>> getAllTargetApps();
    
    @Query("SELECT * FROM target_apps WHERE isEnabled = 1 ORDER BY appName ASC")
    LiveData<List<TargetApp>> getEnabledTargetApps();
    
    @Query("SELECT * FROM target_apps WHERE packageName = :packageName LIMIT 1")
    LiveData<TargetApp> getTargetAppByPackage(String packageName);
    
    @Query("SELECT * FROM target_apps WHERE packageName = :packageName LIMIT 1")
    TargetApp getTargetAppByPackageSync(String packageName);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTargetApp(TargetApp targetApp);
    
    @Update
    void updateTargetApp(TargetApp targetApp);
    
    @Delete
    void deleteTargetApp(TargetApp targetApp);
    
    @Query("UPDATE target_apps SET isEnabled = :enabled WHERE id = :appId")
    void updateEnabledStatus(int appId, boolean enabled);
    
    @Query("UPDATE target_apps SET dailyLimitMinutes = :limit WHERE id = :appId")
    void updateDailyLimit(int appId, int limit);
    
    @Query("UPDATE target_apps SET perUnlockDurationMinutes = :duration WHERE id = :appId")
    void updateUnlockDuration(int appId, int duration);
    
    @Query("UPDATE target_apps SET questionsPerUnlock = :questions WHERE id = :appId")
    void updateQuestionsPerUnlock(int appId, int questions);
    
    @Query("UPDATE target_apps SET difficultyLevel = :difficulty WHERE id = :appId")
    void updateDifficultyLevel(int appId, String difficulty);
    
    @Query("UPDATE target_apps SET selectedTopics = :topics WHERE id = :appId")
    void updateSelectedTopics(int appId, String topics);
    
    @Query("UPDATE target_apps SET currentUsesToday = :uses WHERE id = :appId")
    void updateCurrentUsesToday(int appId, int uses);
    
    @Query("UPDATE target_apps SET emergencyUsesUsed = :uses WHERE id = :appId")
    void updateEmergencyUsesUsed(int appId, int uses);
    
    @Query("UPDATE target_apps SET currentUsesToday = 0")
    void resetDailyUsage();
    
    @Query("UPDATE target_apps SET emergencyUsesUsed = 0")
    void resetEmergencyUsage();
}
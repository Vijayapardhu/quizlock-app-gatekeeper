package com.quizlock.gatekeeper.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.quizlock.gatekeeper.data.model.AppUsage;

import java.util.List;

/**
 * Data Access Object for AppUsage entity
 */
@Dao
public interface AppUsageDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppUsage appUsage);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AppUsage> appUsages);
    
    @Update
    void update(AppUsage appUsage);
    
    @Delete
    void delete(AppUsage appUsage);
    
    @Query("SELECT * FROM app_usage ORDER BY last_used DESC")
    LiveData<List<AppUsage>> getAllAppUsage();
    
    @Query("SELECT * FROM app_usage WHERE is_blocked = 1 ORDER BY app_name ASC")
    LiveData<List<AppUsage>> getBlockedApps();
    
    @Query("SELECT * FROM app_usage WHERE package_name = :packageName")
    AppUsage getAppUsageByPackage(String packageName);
    
    @Query("SELECT * FROM app_usage WHERE package_name = :packageName")
    LiveData<AppUsage> getAppUsageByPackageLive(String packageName);
    
    @Query("SELECT * FROM app_usage WHERE date = :date ORDER BY usage_time_ms DESC")
    LiveData<List<AppUsage>> getAppUsageByDate(String date);
    
    @Query("SELECT SUM(usage_time_ms) FROM app_usage WHERE date = :date")
    long getTotalUsageTimeByDate(String date);
    
    @Query("SELECT SUM(usage_time_ms) FROM app_usage WHERE package_name = :packageName")
    long getTotalUsageTimeByApp(String packageName);
    
    @Query("SELECT COUNT(*) FROM app_usage WHERE is_blocked = 1")
    LiveData<Integer> getBlockedAppCount();
    
    @Query("UPDATE app_usage SET is_blocked = :isBlocked WHERE package_name = :packageName")
    void updateBlockStatus(String packageName, boolean isBlocked);
    
    @Query("UPDATE app_usage SET usage_time_ms = usage_time_ms + :additionalTime, last_used = :timestamp WHERE package_name = :packageName")
    void addUsageTime(String packageName, long additionalTime, long timestamp);
    
    @Query("UPDATE app_usage SET launch_count = launch_count + 1, last_used = :timestamp WHERE package_name = :packageName")
    void incrementLaunchCount(String packageName, long timestamp);
    
    @Query("UPDATE app_usage SET blocked_attempts = blocked_attempts + 1 WHERE package_name = :packageName")
    void incrementBlockedAttempts(String packageName);
    
    @Query("UPDATE app_usage SET successful_unlocks = successful_unlocks + 1, last_unlock = :timestamp WHERE package_name = :packageName")
    void incrementSuccessfulUnlocks(String packageName, long timestamp);
    
    @Query("DELETE FROM app_usage WHERE date < :cutoffDate")
    void deleteOldUsageData(String cutoffDate);
    
    @Query("SELECT * FROM app_usage WHERE usage_time_ms > 0 ORDER BY usage_time_ms DESC LIMIT :limit")
    LiveData<List<AppUsage>> getTopUsedApps(int limit);
    
    @Query("SELECT * FROM app_usage WHERE blocked_attempts > 0 ORDER BY blocked_attempts DESC LIMIT :limit")
    LiveData<List<AppUsage>> getTopBlockedApps(int limit);
}
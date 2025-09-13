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
public interface RestrictedAppDao {
    
    @Query("SELECT * FROM restricted_apps ORDER BY appName ASC")
    LiveData<List<RestrictedApp>> getAllApps();
    
    @Query("SELECT * FROM restricted_apps WHERE isRestricted = 1")
    LiveData<List<RestrictedApp>> getRestrictedApps();
    
    @Query("SELECT * FROM restricted_apps WHERE packageName = :packageName")
    RestrictedApp getAppByPackageName(String packageName);
    
    @Query("SELECT * FROM restricted_apps WHERE packageName = :packageName")
    LiveData<RestrictedApp> getAppByPackageNameLive(String packageName);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertApp(RestrictedApp app);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertApps(List<RestrictedApp> apps);
    
    @Update
    void updateApp(RestrictedApp app);
    
    @Delete
    void deleteApp(RestrictedApp app);
    
    @Query("UPDATE restricted_apps SET usedTimeMinutes = :usedTime WHERE packageName = :packageName")
    void updateUsedTime(String packageName, int usedTime);
    
    @Query("UPDATE restricted_apps SET isRestricted = :isRestricted WHERE packageName = :packageName")
    void updateRestrictionStatus(String packageName, boolean isRestricted);
    
    @Query("UPDATE restricted_apps SET dailyLimitMinutes = :limitMinutes WHERE packageName = :packageName")
    void updateDailyLimit(String packageName, int limitMinutes);
    
    @Query("UPDATE restricted_apps SET usedTimeMinutes = 0, lastResetTimestamp = :timestamp")
    void resetDailyUsage(long timestamp);
    
    @Query("SELECT COUNT(*) FROM restricted_apps WHERE isRestricted = 1")
    LiveData<Integer> getRestrictedAppCount();
}
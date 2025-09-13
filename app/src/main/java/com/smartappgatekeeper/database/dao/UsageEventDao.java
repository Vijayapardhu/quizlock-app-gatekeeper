package com.smartappgatekeeper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.smartappgatekeeper.database.entities.UsageEvent;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for UsageEvent entity
 * FR-004: System shall log all interception events for analytics
 * FR-008: System shall track quiz performance and accuracy
 */
@Dao
public interface UsageEventDao {
    
    @Query("SELECT * FROM usage_events ORDER BY timestamp DESC")
    LiveData<List<UsageEvent>> getAllUsageEvents();
    
    @Query("SELECT * FROM usage_events WHERE packageName = :packageName ORDER BY timestamp DESC")
    LiveData<List<UsageEvent>> getUsageEventsByPackage(String packageName);
    
    @Query("SELECT * FROM usage_events WHERE eventType = :eventType ORDER BY timestamp DESC")
    LiveData<List<UsageEvent>> getUsageEventsByType(String eventType);
    
    @Query("SELECT * FROM usage_events WHERE timestamp >= :startDate AND timestamp <= :endDate ORDER BY timestamp DESC")
    LiveData<List<UsageEvent>> getUsageEventsByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT * FROM usage_events WHERE packageName = :packageName AND timestamp >= :startDate AND timestamp <= :endDate ORDER BY timestamp DESC")
    LiveData<List<UsageEvent>> getUsageEventsByPackageAndDateRange(String packageName, Date startDate, Date endDate);
    
    @Query("SELECT * FROM usage_events WHERE success = 1 ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<UsageEvent>> getRecentSuccessfulEvents(int limit);
    
    @Query("SELECT * FROM usage_events WHERE success = 0 ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<UsageEvent>> getRecentFailedEvents(int limit);
    
    @Query("SELECT * FROM usage_events WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    LiveData<List<UsageEvent>> getUsageEventsBySession(String sessionId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsageEvent(UsageEvent usageEvent);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsageEvents(List<UsageEvent> usageEvents);
    
    @Update
    void updateUsageEvent(UsageEvent usageEvent);
    
    @Delete
    void deleteUsageEvent(UsageEvent usageEvent);
    
    @Query("SELECT COUNT(*) FROM usage_events WHERE packageName = :packageName AND success = 1")
    LiveData<Integer> getSuccessfulAttemptsCount(String packageName);
    
    @Query("SELECT COUNT(*) FROM usage_events WHERE packageName = :packageName AND success = 0")
    LiveData<Integer> getFailedAttemptsCount(String packageName);
    
    @Query("SELECT AVG(timeSpentSeconds) FROM usage_events WHERE packageName = :packageName AND success = 1")
    LiveData<Double> getAverageTimeSpent(String packageName);
    
    @Query("SELECT SUM(coinsEarned) FROM usage_events WHERE packageName = :packageName")
    LiveData<Integer> getTotalCoinsEarned(String packageName);
    
    @Query("SELECT SUM(durationSeconds) FROM usage_events WHERE packageName = :packageName AND eventType = 'app_unlock' AND timestamp >= :startDate AND timestamp <= :endDate")
    LiveData<Long> getTotalUsageTime(String packageName, Date startDate, Date endDate);
    
    @Query("SELECT COUNT(DISTINCT DATE(timestamp)) FROM usage_events WHERE packageName = :packageName AND success = 1 AND timestamp >= :startDate AND timestamp <= :endDate")
    LiveData<Integer> getActiveDaysCount(String packageName, Date startDate, Date endDate);
    
    @Query("DELETE FROM usage_events WHERE timestamp < :cutoffDate")
    void deleteOldEvents(Date cutoffDate);
    
    @Delete
    void delete(UsageEvent event);
}
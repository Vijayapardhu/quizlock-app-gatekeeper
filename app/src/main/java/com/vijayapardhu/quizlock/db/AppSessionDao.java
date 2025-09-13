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
public interface AppSessionDao {
    
    @Query("SELECT * FROM app_sessions ORDER BY startTime DESC")
    LiveData<List<AppSession>> getAllSessions();
    
    @Query("SELECT * FROM app_sessions WHERE packageName = :packageName ORDER BY startTime DESC")
    LiveData<List<AppSession>> getSessionsByPackage(String packageName);
    
    @Query("SELECT * FROM app_sessions WHERE date = :date ORDER BY startTime DESC")
    LiveData<List<AppSession>> getSessionsByDate(String date);
    
    @Query("SELECT * FROM app_sessions WHERE date = :date")
    List<AppSession> getSessionsByDateSync(String date);
    
    @Query("SELECT SUM(durationMinutes) FROM app_sessions WHERE packageName = :packageName AND date = :date")
    int getTotalUsageByPackageAndDate(String packageName, String date);
    
    @Query("SELECT SUM(durationMinutes) FROM app_sessions WHERE date = :date")
    LiveData<Integer> getTotalUsageByDate(String date);
    
    @Query("SELECT COUNT(*) FROM app_sessions WHERE date = :date AND wasQuizAnswered = 1")
    LiveData<Integer> getCorrectQuizzesCountByDate(String date);
    
    @Query("SELECT COUNT(*) FROM app_sessions WHERE date = :date AND wasEmergencyUnlock = 1")
    LiveData<Integer> getEmergencyUnlocksCountByDate(String date);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSession(AppSession session);
    
    @Update
    void updateSession(AppSession session);
    
    @Delete
    void deleteSession(AppSession session);
    
    @Query("DELETE FROM app_sessions WHERE startTime < :timestamp")
    void deleteOldSessions(long timestamp);
    
    @Query("SELECT * FROM app_sessions WHERE endTime = 0 ORDER BY startTime DESC LIMIT 1")
    AppSession getActiveSession();
}
package com.smartappgatekeeper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.smartappgatekeeper.database.entities.Streak;
import java.util.List;

/**
 * Data Access Object for Streak entity
 * FR-030: System shall track daily and weekly streaks
 */
@Dao
public interface StreakDao {
    
    @Query("SELECT * FROM streaks ORDER BY streakType ASC, currentCount DESC")
    LiveData<List<Streak>> getAllStreaks();
    
    @Query("SELECT * FROM streaks WHERE streakType = :type LIMIT 1")
    LiveData<Streak> getStreakByType(String type);
    
    @Query("SELECT * FROM streaks WHERE streakType = :type LIMIT 1")
    Streak getStreakByTypeSync(String type);
    
    @Query("SELECT * FROM streaks WHERE isActive = 1 ORDER BY currentCount DESC")
    LiveData<List<Streak>> getActiveStreaks();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreak(Streak streak);
    
    @Update
    void updateStreak(Streak streak);
    
    @Delete
    void deleteStreak(Streak streak);
    
    @Query("UPDATE streaks SET currentCount = :count, lastActivityDate = :activityDate, lastUpdated = :updatedDate, lastUpdateTime = :updateTime WHERE streakType = :type")
    void updateStreakCount(String type, int count, java.util.Date activityDate, java.util.Date updatedDate, long updateTime);
    
    @Query("UPDATE streaks SET isActive = :active WHERE streakType = :type")
    void updateStreakActiveStatus(String type, boolean active);
    
    @Query("UPDATE streaks SET longestCount = :longest WHERE streakType = :type AND :longest > longestCount")
    void updateLongestCount(String type, int longest);
    
    @Query("UPDATE streaks SET totalQuestionsAnswered = totalQuestionsAnswered + :increment WHERE streakType = :type")
    void incrementQuestionsAnswered(String type, int increment);
    
    @Query("UPDATE streaks SET totalCorrectAnswers = totalCorrectAnswers + :increment WHERE streakType = :type")
    void incrementCorrectAnswers(String type, int increment);
    
    @Query("UPDATE streaks SET totalCoinsEarned = totalCoinsEarned + :increment WHERE streakType = :type")
    void incrementCoinsEarned(String type, int increment);
    
    @Query("UPDATE streaks SET achievements = :achievements WHERE streakType = :type")
    void updateAchievements(String type, String achievements);
    
    @Query("SELECT MAX(currentCount) FROM streaks WHERE streakType = :type")
    LiveData<Integer> getMaxStreakCount(String type);
    
    @Query("SELECT SUM(currentCount) FROM streaks WHERE isActive = 1")
    LiveData<Integer> getTotalActiveStreakCount();
    
    @Query("SELECT SUM(totalQuestionsAnswered) FROM streaks")
    LiveData<Integer> getTotalQuestionsAnswered();
    
    @Query("SELECT SUM(totalCorrectAnswers) FROM streaks")
    LiveData<Integer> getTotalCorrectAnswers();
    
    @Query("SELECT SUM(totalCoinsEarned) FROM streaks")
    LiveData<Integer> getTotalCoinsEarned();
}
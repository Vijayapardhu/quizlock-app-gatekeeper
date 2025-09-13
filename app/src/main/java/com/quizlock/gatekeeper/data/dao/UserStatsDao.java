package com.quizlock.gatekeeper.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.quizlock.gatekeeper.data.model.UserStats;

/**
 * Data Access Object for UserStats entity
 */
@Dao
public interface UserStatsDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserStats userStats);
    
    @Update
    void update(UserStats userStats);
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    LiveData<UserStats> getUserStats();
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    UserStats getUserStatsSync();
    
    @Query("UPDATE user_stats SET current_streak = :streak, updated_at = :timestamp WHERE id = 1")
    void updateCurrentStreak(int streak, long timestamp);
    
    @Query("UPDATE user_stats SET longest_streak = :streak, updated_at = :timestamp WHERE id = 1")
    void updateLongestStreak(int streak, long timestamp);
    
    @Query("UPDATE user_stats SET total_questions_answered = total_questions_answered + 1, updated_at = :timestamp WHERE id = 1")
    void incrementTotalQuestions(long timestamp);
    
    @Query("UPDATE user_stats SET correct_answers = correct_answers + 1, updated_at = :timestamp WHERE id = 1")
    void incrementCorrectAnswers(long timestamp);
    
    @Query("UPDATE user_stats SET experience_points = experience_points + :points, updated_at = :timestamp WHERE id = 1")
    void addExperiencePoints(int points, long timestamp);
    
    @Query("UPDATE user_stats SET level = :level, updated_at = :timestamp WHERE id = 1")
    void updateLevel(int level, long timestamp);
    
    @Query("UPDATE user_stats SET total_time_saved_ms = total_time_saved_ms + :timeMs, updated_at = :timestamp WHERE id = 1")
    void addTimeSaved(long timeMs, long timestamp);
    
    @Query("UPDATE user_stats SET total_study_time_ms = total_study_time_ms + :timeMs, updated_at = :timestamp WHERE id = 1")
    void addStudyTime(long timeMs, long timestamp);
    
    @Query("UPDATE user_stats SET last_activity_date = :date, updated_at = :timestamp WHERE id = 1")
    void updateLastActivityDate(String date, long timestamp);
    
    @Query("UPDATE user_stats SET favorite_category = :category, updated_at = :timestamp WHERE id = 1")
    void updateFavoriteCategory(String category, long timestamp);
    
    @Query("UPDATE user_stats SET badges_earned = :badges, updated_at = :timestamp WHERE id = 1")
    void updateBadges(String badges, long timestamp);
    
    // Initialize user stats if not exists
    @Query("INSERT OR IGNORE INTO user_stats (id) VALUES (1)")
    void initializeUserStats();
}
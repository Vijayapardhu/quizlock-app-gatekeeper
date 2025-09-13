package com.smartappgatekeeper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.smartappgatekeeper.database.entities.UserProfile;
import java.util.List;

/**
 * Data Access Object for UserProfile entity
 */
@Dao
public interface UserProfileDao {
    
    @Query("SELECT * FROM user_profile LIMIT 1")
    LiveData<UserProfile> getUserProfile();
    
    @Query("SELECT * FROM user_profile LIMIT 1")
    UserProfile getUserProfileSync();
    
    @Query("SELECT coins FROM user_profile LIMIT 1")
    LiveData<Integer> getTotalCoins();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProfile(UserProfile userProfile);
    
    @Update
    void updateUserProfile(UserProfile userProfile);
    
    @Delete
    void deleteUserProfile(UserProfile userProfile);
    
    @Query("UPDATE user_profile SET selectedTopics = :topics WHERE id = :userId")
    void updateSelectedTopics(int userId, String topics);
    
    @Query("UPDATE user_profile SET difficultyLevel = :level WHERE id = :userId")
    void updateDifficultyLevel(int userId, int level);
    
    @Query("UPDATE user_profile SET dailyGoalMinutes = :goal WHERE id = :userId")
    void updateDailyGoal(int userId, int goal);
    
    @Query("UPDATE user_profile SET notificationsEnabled = :enabled WHERE id = :userId")
    void updateNotificationsEnabled(int userId, boolean enabled);
    
    @Query("UPDATE user_profile SET socialFeaturesEnabled = :enabled WHERE id = :userId")
    void updateSocialFeaturesEnabled(int userId, boolean enabled);
}
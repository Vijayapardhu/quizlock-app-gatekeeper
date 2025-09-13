package com.smartappgatekeeper.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.smartappgatekeeper.database.dao.*;
import com.smartappgatekeeper.database.entities.*;
import com.smartappgatekeeper.database.converters.DateConverters;

/**
 * Room database for Smart App Gatekeeper
 * Central database containing all app data
 */
@Database(
    entities = {
        UserProfile.class,
        TargetApp.class,
        Question.class,
        UsageEvent.class,
        Streak.class,
        AppSettings.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({DateConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase INSTANCE;
    
    // DAO accessors
    public abstract UserProfileDao userProfileDao();
    public abstract TargetAppDao targetAppDao();
    public abstract QuestionDao questionDao();
    public abstract UsageEventDao usageEventDao();
    public abstract StreakDao streakDao();
    public abstract AppSettingsDao appSettingsDao();
    
    /**
     * Get singleton instance of the database
     * @param context Application context
     * @return AppDatabase instance
     */
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "smart_app_gatekeeper_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * Close database connection
     */
    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
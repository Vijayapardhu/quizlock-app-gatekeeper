package com.smartappgatekeeper.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
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
        UsageEvent.class,
        Streak.class,
        AppSettings.class,
        QuizResult.class
    },
    version = 3,
    exportSchema = false
)
@TypeConverters({DateConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase INSTANCE;
    
    // Migration from version 1 to 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we're using fallbackToDestructiveMigration, 
            // this migration will handle schema changes automatically
            // No specific SQL needed as Room will recreate tables
        }
    };
    
    // Migration from version 2 to 3
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add quiz_results table
            database.execSQL("CREATE TABLE IF NOT EXISTS quiz_results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "quizId TEXT, " +
                "userId TEXT, " +
                "totalQuestions INTEGER NOT NULL, " +
                "correctAnswers INTEGER NOT NULL, " +
                "wrongAnswers INTEGER NOT NULL, " +
                "skippedQuestions INTEGER NOT NULL, " +
                "accuracy REAL NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "timeSpent INTEGER NOT NULL, " +
                "difficulty TEXT, " +
                "topic TEXT, " +
                "completedAt INTEGER, " +
                "completedAtLong INTEGER NOT NULL, " +
                "quizType TEXT, " +
                "isPassed INTEGER NOT NULL, " +
                "passingScore INTEGER NOT NULL, " +
                "status TEXT, " +
                "averageTimePerQuestion REAL NOT NULL, " +
                "streak INTEGER NOT NULL, " +
                "weakAreas TEXT, " +
                "strongAreas TEXT, " +
                "coinsEarned INTEGER NOT NULL, " +
                "experiencePoints INTEGER NOT NULL, " +
                "achievementsUnlocked TEXT, " +
                "isPersonalBest INTEGER NOT NULL, " +
                "deviceInfo TEXT, " +
                "appVersion TEXT, " +
                "notes TEXT, " +
                "isSynced INTEGER NOT NULL)");
        }
    };
    
    // DAO accessors
    public abstract UserProfileDao userProfileDao();
    public abstract TargetAppDao targetAppDao();
    public abstract UsageEventDao usageEventDao();
    public abstract StreakDao streakDao();
    public abstract AppSettingsDao appSettingsDao();
    public abstract QuizResultDao quizResultDao();
    
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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
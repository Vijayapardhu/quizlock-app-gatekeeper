package com.quizlock.gatekeeper.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.quizlock.gatekeeper.data.dao.QuizQuestionDao;
import com.quizlock.gatekeeper.data.dao.QuizHistoryDao;
import com.quizlock.gatekeeper.data.dao.AppUsageDao;
import com.quizlock.gatekeeper.data.dao.UserStatsDao;
import com.quizlock.gatekeeper.data.model.QuizQuestion;
import com.quizlock.gatekeeper.data.model.QuizHistory;
import com.quizlock.gatekeeper.data.model.AppUsage;
import com.quizlock.gatekeeper.data.model.UserStats;

/**
 * Room database for QuizLock application
 */
@Database(
    entities = {
        QuizQuestion.class,
        QuizHistory.class,
        AppUsage.class,
        UserStats.class
    },
    version = 1,
    exportSchema = true
)
public abstract class QuizLockDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "quizlock_database";
    private static volatile QuizLockDatabase INSTANCE;
    
    // Abstract methods to get DAOs
    public abstract QuizQuestionDao quizQuestionDao();
    public abstract QuizHistoryDao quizHistoryDao();
    public abstract AppUsageDao appUsageDao();
    public abstract UserStatsDao userStatsDao();
    
    /**
     * Singleton pattern to get database instance
     */
    public static QuizLockDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (QuizLockDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        QuizLockDatabase.class,
                        DATABASE_NAME
                    )
                    .addCallback(new DatabaseCallback())
                    .fallbackToDestructiveMigration() // For development
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * Database callback to initialize data
     */
    private static class DatabaseCallback extends RoomDatabase.Callback {
        @Override
        public void onCreate(androidx.sqlite.db.SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            // Initialize user stats
            new Thread(() -> {
                if (INSTANCE != null) {
                    INSTANCE.userStatsDao().initializeUserStats();
                    populateInitialQuestions();
                }
            }).start();
        }
        
        /**
         * Populate database with initial quiz questions
         */
        private void populateInitialQuestions() {
            if (INSTANCE == null) return;
            
            QuizQuestionDao dao = INSTANCE.quizQuestionDao();
            
            // Math questions
            dao.insert(new QuizQuestion(
                "What is 15 × 7?",
                "105",
                "95", "105", "115", "125",
                "math", "easy", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "What is the square root of 144?",
                "12",
                "10", "11", "12", "13",
                "math", "easy", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "If a triangle has angles of 60°, 60°, and 60°, what type of triangle is it?",
                "Equilateral",
                "Isosceles", "Equilateral", "Scalene", "Right",
                "math", "medium", "local"
            ));
            
            // Coding questions
            dao.insert(new QuizQuestion(
                "Which data structure follows LIFO (Last In, First Out) principle?",
                "Stack",
                "Queue", "Array", "Stack", "LinkedList",
                "coding", "easy", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "What is the time complexity of binary search?",
                "O(log n)",
                "O(n)", "O(log n)", "O(n²)", "O(1)",
                "coding", "medium", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "In Java, which keyword is used to inherit a class?",
                "extends",
                "implements", "extends", "inherits", "super",
                "coding", "easy", "local"
            ));
            
            // General Knowledge questions
            dao.insert(new QuizQuestion(
                "What is the capital of Australia?",
                "Canberra",
                "Sydney", "Melbourne", "Canberra", "Perth",
                "general", "medium", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "Which planet is known as the Red Planet?",
                "Mars",
                "Venus", "Mars", "Jupiter", "Saturn",
                "general", "easy", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "Who painted the Mona Lisa?",
                "Leonardo da Vinci",
                "Michelangelo", "Leonardo da Vinci", "Picasso", "Van Gogh",
                "general", "easy", "local"
            ));
            
            dao.insert(new QuizQuestion(
                "What is the largest ocean on Earth?",
                "Pacific Ocean",
                "Atlantic Ocean", "Pacific Ocean", "Indian Ocean", "Arctic Ocean",
                "general", "easy", "local"
            ));
        }
    }
    
    /**
     * Close database connection
     */
    public static void closeDatabase() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
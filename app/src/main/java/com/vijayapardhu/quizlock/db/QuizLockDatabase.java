package com.vijayapardhu.quizlock.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        RestrictedApp.class,
        QuizQuestion.class,
        QuizHistory.class,
        UserPreferences.class,
        AppSession.class
    },
    version = 1,
    exportSchema = false
)
public abstract class QuizLockDatabase extends RoomDatabase {
    
    public abstract RestrictedAppDao restrictedAppDao();
    public abstract QuizQuestionDao quizQuestionDao();
    public abstract QuizHistoryDao quizHistoryDao();
    public abstract UserPreferencesDao userPreferencesDao();
    public abstract AppSessionDao appSessionDao();
    
    private static volatile QuizLockDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = 
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    public static QuizLockDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuizLockDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        QuizLockDatabase.class,
                        "quizlock_database"
                    )
                    .addCallback(sRoomDatabaseCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(androidx.sqlite.db.SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            // Populate the database with default offline questions
            databaseWriteExecutor.execute(() -> {
                QuizQuestionDao dao = INSTANCE.quizQuestionDao();
                UserPreferencesDao prefsDao = INSTANCE.userPreferencesDao();
                
                // Insert default preferences
                prefsDao.insertPreference(new UserPreferences("onboarding_completed", "false"));
                prefsDao.insertPreference(new UserPreferences("accessibility_enabled", "false"));
                prefsDao.insertPreference(new UserPreferences("current_streak", "0"));
                prefsDao.insertPreference(new UserPreferences("best_streak", "0"));
                prefsDao.insertPreference(new UserPreferences("selected_topics", "Math,General Knowledge"));
                
                // Insert default offline questions
                insertDefaultQuestions(dao);
            });
        }
    };
    
    private static void insertDefaultQuestions(QuizQuestionDao dao) {
        // Math questions
        dao.insertQuestion(new QuizQuestion(
            "What is 25 × 4?",
            "90", "100", "110", "120",
            "100", "Math", "Easy", false
        ));
        
        dao.insertQuestion(new QuizQuestion(
            "What is the square root of 144?",
            "10", "11", "12", "13",
            "12", "Math", "Easy", false
        ));
        
        dao.insertQuestion(new QuizQuestion(
            "What is 15% of 200?",
            "25", "30", "35", "40",
            "30", "Math", "Medium", false
        ));
        
        // General Knowledge questions
        dao.insertQuestion(new QuizQuestion(
            "What is the capital of France?",
            "London", "Berlin", "Paris", "Madrid",
            "Paris", "General Knowledge", "Easy", false
        ));
        
        dao.insertQuestion(new QuizQuestion(
            "Which planet is known as the Red Planet?",
            "Venus", "Mars", "Jupiter", "Saturn",
            "Mars", "General Knowledge", "Easy", false
        ));
        
        dao.insertQuestion(new QuizQuestion(
            "Who painted the Mona Lisa?",
            "Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo",
            "Leonardo da Vinci", "General Knowledge", "Medium", false
        ));
        
        // Programming questions
        dao.insertQuestion(new QuizQuestion(
            "Which of the following is a programming language?",
            "HTML", "CSS", "Java", "SQL",
            "Java", "Programming", "Easy", false
        ));
        
        dao.insertQuestion(new QuizQuestion(
            "What does 'API' stand for?",
            "Application Programming Interface", "Advanced Programming Interface", 
            "Automated Programming Interface", "Application Process Interface",
            "Application Programming Interface", "Programming", "Medium", false
        ));
        
        // Vocabulary questions
        dao.insertQuestion(new QuizQuestion(
            "What does 'ubiquitous' mean?",
            "Rare", "Present everywhere", "Very large", "Dangerous",
            "Present everywhere", "Vocabulary", "Medium", false
        ));
        
        dao.insertQuestion(new QuizQuestion(
            "What is a synonym for 'happy'?",
            "Sad", "Angry", "Joyful", "Tired",
            "Joyful", "Vocabulary", "Easy", false
        ));
    }
}
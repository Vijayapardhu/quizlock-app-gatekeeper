package com.quizlock.gatekeeper.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.quizlock.gatekeeper.data.database.QuizLockDatabase;
import com.quizlock.gatekeeper.data.dao.QuizQuestionDao;
import com.quizlock.gatekeeper.data.dao.QuizHistoryDao;
import com.quizlock.gatekeeper.data.dao.AppUsageDao;
import com.quizlock.gatekeeper.data.dao.UserStatsDao;
import com.quizlock.gatekeeper.data.model.QuizQuestion;
import com.quizlock.gatekeeper.data.model.QuizHistory;
import com.quizlock.gatekeeper.data.model.AppUsage;
import com.quizlock.gatekeeper.data.model.UserStats;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for managing quiz-related data operations
 */
public class QuizRepository {
    
    private final QuizQuestionDao quizQuestionDao;
    private final QuizHistoryDao quizHistoryDao;
    private final AppUsageDao appUsageDao;
    private final UserStatsDao userStatsDao;
    private final ExecutorService executor;
    private final QuizGenerator quizGenerator;
    
    public QuizRepository(Context context, QuizLockDatabase database) {
        this.quizQuestionDao = database.quizQuestionDao();
        this.quizHistoryDao = database.quizHistoryDao();
        this.appUsageDao = database.appUsageDao();
        this.userStatsDao = database.userStatsDao();
        this.executor = Executors.newFixedThreadPool(4);
        
        // Initialize Gemini API integration
        QuizLockApplication app = (QuizLockApplication) context.getApplicationContext();
        String apiKey = app.getPreferencesManager().getGeminiApiKey();
        if (!apiKey.isEmpty()) {
            this.quizGenerator = new QuizGenerator(
                com.quizlock.gatekeeper.api.ApiClient.getGeminiService(), 
                apiKey
            );
        } else {
            this.quizGenerator = null;
        }
    }
    
    // Quiz Questions operations
    public void insertQuestion(QuizQuestion question) {
        executor.execute(() -> quizQuestionDao.insert(question));
    }
    
    public void insertQuestions(List<QuizQuestion> questions) {
        executor.execute(() -> quizQuestionDao.insertAll(questions));
    }
    
    public LiveData<List<QuizQuestion>> getAllQuestions() {
        return quizQuestionDao.getAllQuestions();
    }
    
    public void getRandomQuestion(String category, String difficulty, QuestionCallback callback) {
        executor.execute(() -> {
            QuizQuestion question;
            if (difficulty != null && !difficulty.isEmpty()) {
                question = quizQuestionDao.getRandomQuestionByCategoryAndDifficulty(category, difficulty);
            } else {
                question = quizQuestionDao.getRandomQuestionByCategory(category);
            }
            
            // If no local question found and API is available, try to generate one
            if (question == null && quizGenerator != null) {
                quizGenerator.generateQuestion(category, difficulty != null ? difficulty : "medium", 
                    new com.quizlock.gatekeeper.api.QuizGenerator.QuestionCallback() {
                        @Override
                        public void onQuestionGenerated(QuizQuestion generatedQuestion) {
                            // Save generated question to database
                            insertQuestion(generatedQuestion);
                            callback.onQuestionLoaded(generatedQuestion);
                        }
                        
                        @Override
                        public void onError(String error) {
                            // Fallback to any available local question
                            QuizQuestion fallbackQuestion = quizQuestionDao.getRandomQuestionByCategory("general");
                            callback.onQuestionLoaded(fallbackQuestion);
                        }
                    });
            } else {
                callback.onQuestionLoaded(question);
            }
        });
    }
    
    public LiveData<List<QuizQuestion>> getQuestionsByCategory(String category) {
        return quizQuestionDao.getQuestionsByCategory(category);
    }
    
    public LiveData<Integer> getQuestionCount() {
        return quizQuestionDao.getQuestionCount();
    }
    
    // Quiz History operations
    public void insertHistory(QuizHistory history) {
        executor.execute(() -> {
            quizHistoryDao.insert(history);
            updateUserStats(history);
        });
    }
    
    public LiveData<List<QuizHistory>> getAllHistory() {
        return quizHistoryDao.getAllHistory();
    }
    
    public LiveData<List<QuizHistory>> getRecentHistory(int limit) {
        return quizHistoryDao.getRecentHistory(limit);
    }
    
    public LiveData<Integer> getTotalQuestionCount() {
        return quizHistoryDao.getTotalQuestionCount();
    }
    
    public LiveData<Integer> getCorrectAnswerCount() {
        return quizHistoryDao.getCorrectAnswerCount();
    }
    
    // App Usage operations
    public void insertOrUpdateAppUsage(String packageName, String appName, boolean isBlocked) {
        executor.execute(() -> {
            AppUsage existingUsage = appUsageDao.getAppUsageByPackage(packageName);
            if (existingUsage == null) {
                appUsageDao.insert(new AppUsage(packageName, appName, isBlocked));
            } else {
                existingUsage.setBlocked(isBlocked);
                appUsageDao.update(existingUsage);
            }
        });
    }
    
    public LiveData<List<AppUsage>> getBlockedApps() {
        return appUsageDao.getBlockedApps();
    }
    
    public LiveData<List<AppUsage>> getAllAppUsage() {
        return appUsageDao.getAllAppUsage();
    }
    
    public void getAppUsage(String packageName, AppUsageCallback callback) {
        executor.execute(() -> {
            AppUsage usage = appUsageDao.getAppUsageByPackage(packageName);
            callback.onAppUsageLoaded(usage);
        });
    }
    
    public void recordAppLaunch(String packageName) {
        executor.execute(() -> {
            long timestamp = System.currentTimeMillis();
            appUsageDao.incrementLaunchCount(packageName, timestamp);
        });
    }
    
    public void recordBlockedAttempt(String packageName) {
        executor.execute(() -> appUsageDao.incrementBlockedAttempts(packageName));
    }
    
    public void recordSuccessfulUnlock(String packageName) {
        executor.execute(() -> {
            long timestamp = System.currentTimeMillis();
            appUsageDao.incrementSuccessfulUnlocks(packageName, timestamp);
        });
    }
    
    public void updateBlockStatus(String packageName, boolean isBlocked) {
        executor.execute(() -> appUsageDao.updateBlockStatus(packageName, isBlocked));
    }
    
    // User Stats operations
    public LiveData<UserStats> getUserStats() {
        return userStatsDao.getUserStats();
    }
    
    public void getUserStatsSync(UserStatsCallback callback) {
        executor.execute(() -> {
            UserStats stats = userStatsDao.getUserStatsSync();
            if (stats == null) {
                stats = new UserStats();
                userStatsDao.insert(stats);
            }
            callback.onUserStatsLoaded(stats);
        });
    }
    
    private void updateUserStats(QuizHistory history) {
        UserStats stats = userStatsDao.getUserStatsSync();
        if (stats == null) {
            stats = new UserStats();
            userStatsDao.insert(stats);
        } else {
            stats.recordAnswer(history.isCorrect(), history.getTimeTakenMs());
            userStatsDao.update(stats);
        }
    }
    
    public void updateUserExperience(int points) {
        executor.execute(() -> {
            long timestamp = System.currentTimeMillis();
            userStatsDao.addExperiencePoints(points, timestamp);
        });
    }
    
    public void updateFavoriteCategory(String category) {
        executor.execute(() -> {
            long timestamp = System.currentTimeMillis();
            userStatsDao.updateFavoriteCategory(category, timestamp);
        });
    }
    
    // Cleanup operations
    public void cleanupOldData() {
        executor.execute(() -> {
            long cutoffTime = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000); // 30 days
            quizHistoryDao.deleteOldHistory(cutoffTime);
            
            // Keep app usage data for 90 days
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            String cutoffDate = sdf.format(new java.util.Date(System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000)));
            appUsageDao.deleteOldUsageData(cutoffDate);
        });
    }
    
    // Callback interfaces
    public interface QuestionCallback {
        void onQuestionLoaded(QuizQuestion question);
    }
    
    public interface AppUsageCallback {
        void onAppUsageLoaded(AppUsage appUsage);
    }
    
    public interface UserStatsCallback {
        void onUserStatsLoaded(UserStats userStats);
    }
}
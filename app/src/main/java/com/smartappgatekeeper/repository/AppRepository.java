package com.smartappgatekeeper.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.smartappgatekeeper.database.AppDatabase;
import com.smartappgatekeeper.database.dao.*;
import com.smartappgatekeeper.database.entities.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository pattern implementation for data access
 * Central data management layer
 */
public class AppRepository {
    private static volatile AppRepository INSTANCE;
    private final AppDatabase database;
    private final ExecutorService executor;
    
    // DAOs
    private final UserProfileDao userProfileDao;
    private final TargetAppDao targetAppDao;
    private final QuestionDao questionDao;
    private final UsageEventDao usageEventDao;
    private final StreakDao streakDao;
    private final AppSettingsDao appSettingsDao;
    
    private AppRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        executor = Executors.newFixedThreadPool(4);
        
        // Initialize DAOs
        userProfileDao = database.userProfileDao();
        targetAppDao = database.targetAppDao();
        questionDao = database.questionDao();
        usageEventDao = database.usageEventDao();
        streakDao = database.streakDao();
        appSettingsDao = database.appSettingsDao();
    }
    
    public static AppRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository(application);
                }
            }
        }
        return INSTANCE;
    }
    
    // User Profile methods
    public LiveData<UserProfile> getUserProfile() {
        return userProfileDao.getUserProfile();
    }
    
    public CompletableFuture<UserProfile> getUserProfileAsync() {
        return CompletableFuture.supplyAsync(() -> userProfileDao.getUserProfileSync(), executor);
    }
    
    public void insertUserProfile(UserProfile userProfile) {
        executor.execute(() -> userProfileDao.insertUserProfile(userProfile));
    }
    
    public void updateUserProfile(UserProfile userProfile) {
        executor.execute(() -> userProfileDao.updateUserProfile(userProfile));
    }
    
    // Target App methods
    public LiveData<List<TargetApp>> getAllTargetApps() {
        return targetAppDao.getAllTargetApps();
    }
    
    public LiveData<List<TargetApp>> getEnabledTargetApps() {
        return targetAppDao.getEnabledTargetApps();
    }
    
    public LiveData<TargetApp> getTargetAppByPackage(String packageName) {
        return targetAppDao.getTargetAppByPackage(packageName);
    }
    
    public CompletableFuture<TargetApp> getTargetAppByPackageAsync(String packageName) {
        return CompletableFuture.supplyAsync(() -> targetAppDao.getTargetAppByPackageSync(packageName), executor);
    }
    
    public void insertTargetApp(TargetApp targetApp) {
        executor.execute(() -> targetAppDao.insertTargetApp(targetApp));
    }
    
    public void updateTargetApp(TargetApp targetApp) {
        executor.execute(() -> targetAppDao.updateTargetApp(targetApp));
    }
    
    public void deleteTargetApp(TargetApp targetApp) {
        executor.execute(() -> targetAppDao.deleteTargetApp(targetApp));
    }
    
    // Question methods
    public LiveData<List<Question>> getRandomQuestions(int limit) {
        return questionDao.getRandomQuestions(limit);
    }
    
    public LiveData<List<Question>> getRandomQuestionsByTopic(String topic, int limit) {
        return questionDao.getRandomQuestionsByTopic(topic, limit);
    }
    
    public LiveData<List<Question>> getRandomQuestionsByDifficulty(String difficulty, int limit) {
        return questionDao.getRandomQuestionsByDifficulty(difficulty, limit);
    }
    
    public LiveData<List<Question>> getRandomQuestionsByTopicAndDifficulty(String topic, String difficulty, int limit) {
        return questionDao.getRandomQuestionsByTopicAndDifficulty(topic, difficulty, limit);
    }
    
    public CompletableFuture<Question> getQuestionByIdAsync(int id) {
        return CompletableFuture.supplyAsync(() -> questionDao.getQuestionByIdSync(id), executor);
    }
    
    public void insertQuestion(Question question) {
        executor.execute(() -> questionDao.insertQuestion(question));
    }
    
    public void insertQuestions(List<Question> questions) {
        executor.execute(() -> questionDao.insertQuestions(questions));
    }
    
    // Usage Event methods
    public LiveData<List<UsageEvent>> getAllUsageEvents() {
        return usageEventDao.getAllUsageEvents();
    }
    
    public LiveData<List<UsageEvent>> getUsageEventsByPackage(String packageName) {
        return usageEventDao.getUsageEventsByPackage(packageName);
    }
    
    public LiveData<List<UsageEvent>> getUsageEventsByDateRange(java.util.Date startDate, java.util.Date endDate) {
        return usageEventDao.getUsageEventsByDateRange(startDate, endDate);
    }
    
    public void insertUsageEvent(UsageEvent usageEvent) {
        executor.execute(() -> usageEventDao.insertUsageEvent(usageEvent));
    }
    
    public void insertUsageEvents(List<UsageEvent> usageEvents) {
        executor.execute(() -> usageEventDao.insertUsageEvents(usageEvents));
    }
    
    // Streak methods
    public LiveData<List<Streak>> getAllStreaks() {
        return streakDao.getAllStreaks();
    }
    
    public LiveData<Streak> getStreakByType(String type) {
        return streakDao.getStreakByType(type);
    }
    
    public CompletableFuture<Streak> getStreakByTypeAsync(String type) {
        return CompletableFuture.supplyAsync(() -> streakDao.getStreakByTypeSync(type), executor);
    }
    
    public void insertStreak(Streak streak) {
        executor.execute(() -> streakDao.insertStreak(streak));
    }
    
    public void updateStreak(Streak streak) {
        executor.execute(() -> streakDao.updateStreak(streak));
    }
    
    // App Settings methods
    public LiveData<AppSettings> getAppSettings() {
        return appSettingsDao.getAppSettings();
    }
    
    public CompletableFuture<AppSettings> getAppSettingsAsync() {
        return CompletableFuture.supplyAsync(() -> appSettingsDao.getAppSettingsSync(), executor);
    }
    
    public void insertAppSettings(AppSettings appSettings) {
        executor.execute(() -> appSettingsDao.insertAppSettings(appSettings));
    }
    
    public void updateAppSettings(AppSettings appSettings) {
        executor.execute(() -> appSettingsDao.updateAppSettings(appSettings));
    }
    
    // Analytics methods
    public LiveData<Integer> getTotalCoinsEarned() {
        return streakDao.getTotalCoinsEarned();
    }
    
    public LiveData<Integer> getTotalQuestionsAnswered() {
        return streakDao.getTotalQuestionsAnswered();
    }
    
    public LiveData<Integer> getTotalCorrectAnswers() {
        return streakDao.getTotalCorrectAnswers();
    }
    
    public LiveData<Integer> getCurrentStreak() {
        return Transformations.map(streakDao.getStreakByType("daily"), streak -> streak != null ? streak.currentCount : 0);
    }
    
    // Missing methods for compatibility
    public LiveData<List<Question>> getAllQuestions() {
        return questionDao.getAllQuestions();
    }
    
    public LiveData<Integer> getTotalCoins() {
        return userProfileDao.getTotalCoins();
    }
    
    public void deleteUsageEvent(UsageEvent event) {
        executor.execute(() -> usageEventDao.delete(event));
    }
    
    // Cleanup
    public void close() {
        executor.shutdown();
        AppDatabase.closeDatabase();
    }
}
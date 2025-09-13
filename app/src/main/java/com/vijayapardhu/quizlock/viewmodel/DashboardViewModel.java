package com.vijayapardhu.quizlock.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vijayapardhu.quizlock.db.AppSession;
import com.vijayapardhu.quizlock.db.AppSessionDao;
import com.vijayapardhu.quizlock.db.QuizHistoryDao;
import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.RestrictedApp;
import com.vijayapardhu.quizlock.db.RestrictedAppDao;
import com.vijayapardhu.quizlock.db.UserPreferencesDao;
import com.vijayapardhu.quizlock.utils.AppUtils;
import com.vijayapardhu.quizlock.utils.Constants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardViewModel extends AndroidViewModel {
    
    private final RestrictedAppDao restrictedAppDao;
    private final QuizHistoryDao quizHistoryDao;
    private final AppSessionDao appSessionDao;
    private final UserPreferencesDao userPreferencesDao;
    private final ExecutorService executor;
    
    private final LiveData<List<RestrictedApp>> restrictedApps;
    private final LiveData<Integer> todayCorrectAnswers;
    private final LiveData<Integer> todayTotalQuestions;
    private final LiveData<Integer> totalCorrectAnswers;
    private final LiveData<Integer> todayTotalUsage;
    private final LiveData<Integer> correctQuizzesToday;
    private final LiveData<Integer> emergencyUnlocksToday;
    
    private final MutableLiveData<String> currentStreak = new MutableLiveData<>("0");
    private final MutableLiveData<String> bestStreak = new MutableLiveData<>("0");
    private final MutableLiveData<List<DailyUsageStats>> weeklyStats = new MutableLiveData<>();
    private final MutableLiveData<List<TopicPerformance>> topicStats = new MutableLiveData<>();
    
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        QuizLockDatabase database = QuizLockDatabase.getDatabase(application);
        restrictedAppDao = database.restrictedAppDao();
        quizHistoryDao = database.quizHistoryDao();
        appSessionDao = database.appSessionDao();
        userPreferencesDao = database.userPreferencesDao();
        executor = Executors.newFixedThreadPool(2);
        
        // Initialize LiveData
        restrictedApps = restrictedAppDao.getRestrictedApps();
        todayCorrectAnswers = quizHistoryDao.getTodayCorrectAnswers();
        todayTotalQuestions = quizHistoryDao.getTodayTotalQuestions();
        totalCorrectAnswers = quizHistoryDao.getTotalCorrectAnswers();
        
        String today = AppUtils.getCurrentDate();
        todayTotalUsage = appSessionDao.getTotalUsageByDate(today);
        correctQuizzesToday = appSessionDao.getCorrectQuizzesCountByDate(today);
        emergencyUnlocksToday = appSessionDao.getEmergencyUnlocksCountByDate(today);
        
        loadUserStats();
        loadWeeklyStats();
        loadTopicStats();
    }
    
    private void loadUserStats() {
        executor.execute(() -> {
            String currentStreakValue = userPreferencesDao.getPreference(Constants.PREF_CURRENT_STREAK);
            String bestStreakValue = userPreferencesDao.getPreference(Constants.PREF_BEST_STREAK);
            
            currentStreak.postValue(currentStreakValue != null ? currentStreakValue : "0");
            bestStreak.postValue(bestStreakValue != null ? bestStreakValue : "0");
        });
    }
    
    private void loadWeeklyStats() {
        executor.execute(() -> {
            // Calculate stats for last 7 days
            java.util.List<DailyUsageStats> stats = new java.util.ArrayList<>();
            
            for (int i = 6; i >= 0; i--) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DAY_OF_YEAR, -i);
                String date = new java.text.SimpleDateFormat(Constants.DATE_FORMAT, java.util.Locale.getDefault())
                    .format(cal.getTime());
                
                List<AppSession> sessions = appSessionDao.getSessionsByDateSync(date);
                int totalMinutes = 0;
                int quizCount = 0;
                
                for (AppSession session : sessions) {
                    totalMinutes += session.getDurationMinutes();
                    if (session.isWasQuizAnswered()) {
                        quizCount++;
                    }
                }
                
                String dayName = new java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
                    .format(cal.getTime());
                
                stats.add(new DailyUsageStats(dayName, totalMinutes, quizCount));
            }
            
            weeklyStats.postValue(stats);
        });
    }
    
    private void loadTopicStats() {
        executor.execute(() -> {
            // This would typically use the TopicStatistic from QuizHistoryDao
            // For now, creating a simplified version
            java.util.List<TopicPerformance> stats = new java.util.ArrayList<>();
            
            String[] topics = {
                Constants.TOPIC_MATH,
                Constants.TOPIC_PROGRAMMING,
                Constants.TOPIC_GENERAL_KNOWLEDGE,
                Constants.TOPIC_VOCABULARY
            };
            
            for (String topic : topics) {
                int count = quizHistoryDao.getQuestionCountByTopic(topic);
                if (count > 0) {
                    stats.add(new TopicPerformance(topic, count, 85 + (count % 15))); // Mock accuracy
                }
            }
            
            topicStats.postValue(stats);
        });
    }
    
    public void refreshStats() {
        loadUserStats();
        loadWeeklyStats();
        loadTopicStats();
    }
    
    // Getters for LiveData
    public LiveData<List<RestrictedApp>> getRestrictedApps() { return restrictedApps; }
    public LiveData<Integer> getTodayCorrectAnswers() { return todayCorrectAnswers; }
    public LiveData<Integer> getTodayTotalQuestions() { return todayTotalQuestions; }
    public LiveData<Integer> getTotalCorrectAnswers() { return totalCorrectAnswers; }
    public LiveData<Integer> getTodayTotalUsage() { return todayTotalUsage; }
    public LiveData<Integer> getCorrectQuizzesToday() { return correctQuizzesToday; }
    public LiveData<Integer> getEmergencyUnlocksToday() { return emergencyUnlocksToday; }
    
    public LiveData<String> getCurrentStreak() { return currentStreak; }
    public LiveData<String> getBestStreak() { return bestStreak; }
    public LiveData<List<DailyUsageStats>> getWeeklyStats() { return weeklyStats; }
    public LiveData<List<TopicPerformance>> getTopicStats() { return topicStats; }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
    
    // Data classes for statistics
    public static class DailyUsageStats {
        public final String day;
        public final int usageMinutes;
        public final int quizCount;
        
        public DailyUsageStats(String day, int usageMinutes, int quizCount) {
            this.day = day;
            this.usageMinutes = usageMinutes;
            this.quizCount = quizCount;
        }
    }
    
    public static class TopicPerformance {
        public final String topic;
        public final int questionsAnswered;
        public final int accuracyPercentage;
        
        public TopicPerformance(String topic, int questionsAnswered, int accuracyPercentage) {
            this.topic = topic;
            this.questionsAnswered = questionsAnswered;
            this.accuracyPercentage = accuracyPercentage;
        }
    }
}
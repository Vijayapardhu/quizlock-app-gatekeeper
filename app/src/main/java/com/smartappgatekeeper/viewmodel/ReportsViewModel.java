package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.UsageEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel for ReportsFragment
 * Manages analytics, reports, and usage statistics
 */
public class ReportsViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    private final MutableLiveData<UsageStats> todayStats = new MutableLiveData<>();
    private final MutableLiveData<UsageStats> weekStats = new MutableLiveData<>();
    private final MutableLiveData<UsageStats> monthStats = new MutableLiveData<>();
    private final MutableLiveData<List<AppUsage>> topApps = new MutableLiveData<>();
    private final MutableLiveData<List<DailyProgress>> weeklyProgress = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public ReportsViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        loadData();
    }
    
    /**
     * Load all reports data
     */
    private void loadData() {
        loadTodayStats();
        loadWeekStats();
        loadMonthStats();
        loadTopApps();
        loadWeeklyProgress();
    }
    
    /**
     * Load today's usage statistics
     */
    private void loadTodayStats() {
        repository.getAllUsageEvents().observeForever(events -> {
            if (events != null) {
                UsageStats stats = calculateUsageStats(events, getTodayStart(), getTodayEnd());
                todayStats.setValue(stats);
            } else {
                todayStats.setValue(new UsageStats(0, 0, 0, 0));
            }
        });
    }
    
    /**
     * Load this week's usage statistics
     */
    private void loadWeekStats() {
        repository.getAllUsageEvents().observeForever(events -> {
            if (events != null) {
                UsageStats stats = calculateUsageStats(events, getWeekStart(), getWeekEnd());
                weekStats.setValue(stats);
            } else {
                weekStats.setValue(new UsageStats(0, 0, 0, 0));
            }
        });
    }
    
    /**
     * Load this month's usage statistics
     */
    private void loadMonthStats() {
        repository.getAllUsageEvents().observeForever(events -> {
            if (events != null) {
                UsageStats stats = calculateUsageStats(events, getMonthStart(), getMonthEnd());
                monthStats.setValue(stats);
            } else {
                monthStats.setValue(new UsageStats(0, 0, 0, 0));
            }
        });
    }
    
    /**
     * Load top used apps
     */
    private void loadTopApps() {
        repository.getAllUsageEvents().observeForever(events -> {
            if (events != null) {
                List<AppUsage> topAppsList = calculateTopApps(events);
                topApps.setValue(topAppsList);
            } else {
                topApps.setValue(new ArrayList<>());
            }
        });
    }
    
    /**
     * Load weekly progress data
     */
    private void loadWeeklyProgress() {
        List<DailyProgress> progress = new ArrayList<>();
        
        Calendar cal = Calendar.getInstance();
        for (int i = 6; i >= 0; i--) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            Date date = cal.getTime();
            
            // Mock data - in real app, calculate from actual usage
            int questionsAnswered = (int) (Math.random() * 20);
            int timeSaved = questionsAnswered * 2; // 2 minutes per question
            int streak = i < 3 ? i + 1 : 0; // Mock streak pattern
            
            progress.add(new DailyProgress(date, questionsAnswered, timeSaved, streak));
        }
        
        weeklyProgress.setValue(progress);
    }
    
    /**
     * Calculate usage statistics for a time period
     */
    private UsageStats calculateUsageStats(List<UsageEvent> events, long startTime, long endTime) {
        int totalQuestions = 0;
        int correctAnswers = 0;
        int timeSaved = 0;
        int sessionsCompleted = 0;
        
        for (UsageEvent event : events) {
            if (event.getTimestampLong() >= startTime && event.getTimestampLong() <= endTime) {
                totalQuestions++;
                if (event.getEventType().equals("quiz_completed")) {
                    correctAnswers++;
                    timeSaved += 2; // 2 minutes saved per correct answer
                }
                if (event.getEventType().equals("session_completed")) {
                    sessionsCompleted++;
                }
            }
        }
        
        return new UsageStats(totalQuestions, correctAnswers, timeSaved, sessionsCompleted);
    }
    
    /**
     * Calculate top used apps
     */
    private List<AppUsage> calculateTopApps(List<UsageEvent> events) {
        Map<String, Integer> appUsageCount = new HashMap<>();
        
        for (UsageEvent event : events) {
            String appName = event.getAppName();
            if (appName != null) {
                appUsageCount.put(appName, appUsageCount.getOrDefault(appName, 0) + 1);
            }
        }
        
        List<AppUsage> topAppsList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : appUsageCount.entrySet()) {
            topAppsList.add(new AppUsage(entry.getKey(), entry.getValue()));
        }
        
        // Sort by usage count (descending)
        topAppsList.sort((a, b) -> Integer.compare(b.getUsageCount(), a.getUsageCount()));
        
        // Return top 5
        return topAppsList.size() > 5 ? topAppsList.subList(0, 5) : topAppsList;
    }
    
    /**
     * Get today's start time
     */
    private long getTodayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get today's end time
     */
    private long getTodayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get week's start time
     */
    private long getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get week's end time
     */
    private long getWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get month's start time
     */
    private long getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get month's end time
     */
    private long getMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
    
    /**
     * Refresh all data
     */
    public void refreshData() {
        loadData();
    }
    
    // Getters
    public LiveData<UsageStats> getTodayStats() {
        return todayStats;
    }
    
    public LiveData<UsageStats> getWeekStats() {
        return weekStats;
    }
    
    public LiveData<UsageStats> getMonthStats() {
        return monthStats;
    }
    
    public LiveData<List<AppUsage>> getTopApps() {
        return topApps;
    }
    
    public LiveData<List<DailyProgress>> getWeeklyProgress() {
        return weeklyProgress;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Usage statistics data class
     */
    public static class UsageStats {
        private int totalQuestions;
        private int correctAnswers;
        private int timeSaved; // in minutes
        private int sessionsCompleted;
        
        public UsageStats(int totalQuestions, int correctAnswers, int timeSaved, int sessionsCompleted) {
            this.totalQuestions = totalQuestions;
            this.correctAnswers = correctAnswers;
            this.timeSaved = timeSaved;
            this.sessionsCompleted = sessionsCompleted;
        }
        
        // Getters and setters
        public int getTotalQuestions() { return totalQuestions; }
        public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
        
        public int getCorrectAnswers() { return correctAnswers; }
        public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
        
        public int getTimeSaved() { return timeSaved; }
        public void setTimeSaved(int timeSaved) { this.timeSaved = timeSaved; }
        
        public int getSessionsCompleted() { return sessionsCompleted; }
        public void setSessionsCompleted(int sessionsCompleted) { this.sessionsCompleted = sessionsCompleted; }
        
        public double getAccuracy() {
            return totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        }
    }
    
    /**
     * App usage data class
     */
    public static class AppUsage {
        private String appName;
        private int usageCount;
        
        public AppUsage(String appName, int usageCount) {
            this.appName = appName;
            this.usageCount = usageCount;
        }
        
        // Getters and setters
        public String getAppName() { return appName; }
        public void setAppName(String appName) { this.appName = appName; }
        
        public int getUsageCount() { return usageCount; }
        public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    }
    
    /**
     * Daily progress data class
     */
    public static class DailyProgress {
        private Date date;
        private int questionsAnswered;
        private int timeSaved;
        private int streak;
        
        public DailyProgress(Date date, int questionsAnswered, int timeSaved, int streak) {
            this.date = date;
            this.questionsAnswered = questionsAnswered;
            this.timeSaved = timeSaved;
            this.streak = streak;
        }
        
        // Getters and setters
        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
        
        public int getQuestionsAnswered() { return questionsAnswered; }
        public void setQuestionsAnswered(int questionsAnswered) { this.questionsAnswered = questionsAnswered; }
        
        public int getTimeSaved() { return timeSaved; }
        public void setTimeSaved(int timeSaved) { this.timeSaved = timeSaved; }
        
        public int getStreak() { return streak; }
        public void setStreak(int streak) { this.streak = streak; }
    }
}
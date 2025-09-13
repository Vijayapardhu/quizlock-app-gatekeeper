package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.UsageEvent;
import com.smartappgatekeeper.database.entities.Streak;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * ViewModel for Dashboard Fragment
 * Manages dashboard data and business logic
 */
public class DashboardViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    
    // LiveData for UI
    private final MutableLiveData<String> todayUsage = new MutableLiveData<>();
    private final MutableLiveData<String> weeklyUsage = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentStreak = new MutableLiveData<>();
    private final MutableLiveData<Integer> questionsAnswered = new MutableLiveData<>();
    private final MutableLiveData<Double> accuracyRate = new MutableLiveData<>();
    private final MutableLiveData<Integer> coinsEarned = new MutableLiveData<>();
    private final MutableLiveData<Integer> coursesCompleted = new MutableLiveData<>();
    private final MutableLiveData<List<UsageEvent>> recentActivity = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public DashboardViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }
    
    /**
     * Load all dashboard data
     */
    public void loadDashboardData() {
        loadTodayUsage();
        loadWeeklyUsage();
        loadCurrentStreak();
        loadQuestionsAnswered();
        loadAccuracyRate();
        loadCoinsEarned();
        loadCoursesCompleted();
        loadRecentActivity();
    }
    
    /**
     * Load today's usage statistics
     */
    private void loadTodayUsage() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endOfDay = calendar.getTime();
        
        repository.getUsageEventsByDateRange(startOfDay, endOfDay).observeForever(events -> {
            if (events != null) {
                long totalMinutes = calculateTotalUsageMinutes(events);
                todayUsage.setValue(formatUsageTime(totalMinutes));
            }
        });
    }
    
    /**
     * Load weekly usage statistics
     */
    private void loadWeeklyUsage() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfWeek = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date endOfWeek = calendar.getTime();
        
        repository.getUsageEventsByDateRange(startOfWeek, endOfWeek).observeForever(events -> {
            if (events != null) {
                long totalMinutes = calculateTotalUsageMinutes(events);
                weeklyUsage.setValue(formatUsageTime(totalMinutes));
            }
        });
    }
    
    /**
     * Load current streak
     */
    private void loadCurrentStreak() {
        repository.getStreakByType("daily").observeForever(streak -> {
            if (streak != null) {
                currentStreak.setValue(streak.currentCount);
            } else {
                currentStreak.setValue(0);
            }
        });
    }
    
    /**
     * Load questions answered count
     */
    private void loadQuestionsAnswered() {
        repository.getTotalQuestionsAnswered().observeForever(count -> {
            if (count != null) {
                questionsAnswered.setValue(count);
            } else {
                questionsAnswered.setValue(0);
            }
        });
    }
    
    /**
     * Load accuracy rate
     */
    private void loadAccuracyRate() {
        CompletableFuture<Integer> totalQuestionsFuture = repository.getTotalQuestionsAnswered().getValue() != null ?
            CompletableFuture.completedFuture(repository.getTotalQuestionsAnswered().getValue()) :
            CompletableFuture.completedFuture(0);
            
        CompletableFuture<Integer> correctAnswersFuture = repository.getTotalCorrectAnswers().getValue() != null ?
            CompletableFuture.completedFuture(repository.getTotalCorrectAnswers().getValue()) :
            CompletableFuture.completedFuture(0);
        
        CompletableFuture.allOf(totalQuestionsFuture, correctAnswersFuture).thenRun(() -> {
            int total = totalQuestionsFuture.join();
            int correct = correctAnswersFuture.join();
            
            if (total > 0) {
                double accuracy = (double) correct / total * 100;
                accuracyRate.setValue(accuracy);
            } else {
                accuracyRate.setValue(0.0);
            }
        });
    }
    
    /**
     * Load coins earned
     */
    private void loadCoinsEarned() {
        repository.getTotalCoinsEarned().observeForever(coins -> {
            if (coins != null) {
                coinsEarned.setValue(coins);
            } else {
                coinsEarned.setValue(0);
            }
        });
    }
    
    /**
     * Load courses completed count
     */
    private void loadCoursesCompleted() {
        // TODO: Implement course completion tracking
        coursesCompleted.setValue(0);
    }
    
    /**
     * Load recent activity
     */
    private void loadRecentActivity() {
        repository.getAllUsageEvents().observeForever(events -> {
            if (events != null) {
                // Get last 10 events
                List<UsageEvent> recentEvents = events.size() > 10 ? 
                    events.subList(0, 10) : events;
                recentActivity.setValue(recentEvents);
            } else {
                recentActivity.setValue(new ArrayList<>());
            }
        });
    }
    
    /**
     * Calculate total usage minutes from events
     */
    private long calculateTotalUsageMinutes(List<UsageEvent> events) {
        long totalSeconds = 0;
        for (UsageEvent event : events) {
            if ("app_unlock".equals(event.eventType) && event.durationSeconds > 0) {
                totalSeconds += event.durationSeconds;
            }
        }
        return totalSeconds / 60; // Convert to minutes
    }
    
    /**
     * Format usage time for display
     */
    private String formatUsageTime(long minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            long hours = minutes / 60;
            long remainingMinutes = minutes % 60;
            return hours + "h " + remainingMinutes + "m";
        }
    }
    
    // Getters for LiveData
    public LiveData<String> getTodayUsage() { return todayUsage; }
    public LiveData<String> getWeeklyUsage() { return weeklyUsage; }
    public LiveData<Integer> getCurrentStreak() { return currentStreak; }
    public LiveData<Integer> getQuestionsAnswered() { return questionsAnswered; }
    public LiveData<Double> getAccuracyRate() { return accuracyRate; }
    public LiveData<Integer> getCoinsEarned() { return coinsEarned; }
    public LiveData<Integer> getCoursesCompleted() { return coursesCompleted; }
    public LiveData<List<UsageEvent>> getRecentActivity() { return recentActivity; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
}
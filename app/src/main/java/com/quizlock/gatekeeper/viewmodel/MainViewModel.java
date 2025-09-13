package com.quizlock.gatekeeper.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quizlock.gatekeeper.data.model.AppUsage;
import com.quizlock.gatekeeper.data.model.QuizHistory;
import com.quizlock.gatekeeper.data.model.UserStats;
import com.quizlock.gatekeeper.data.repository.QuizRepository;

import java.util.List;

/**
 * ViewModel for MainActivity
 */
public class MainViewModel extends ViewModel {
    
    private final QuizRepository repository;
    private final LiveData<UserStats> userStats;
    private final LiveData<List<AppUsage>> blockedApps;
    private final LiveData<List<QuizHistory>> recentHistory;
    
    public MainViewModel(QuizRepository repository) {
        this.repository = repository;
        this.userStats = repository.getUserStats();
        this.blockedApps = repository.getBlockedApps();
        this.recentHistory = repository.getRecentHistory(5); // Last 5 entries
    }
    
    public LiveData<UserStats> getUserStats() {
        return userStats;
    }
    
    public LiveData<List<AppUsage>> getBlockedApps() {
        return blockedApps;
    }
    
    public LiveData<List<QuizHistory>> getRecentHistory() {
        return recentHistory;
    }
    
    public void refreshData() {
        // Trigger data refresh if needed
        // For now, LiveData will automatically update
    }
}
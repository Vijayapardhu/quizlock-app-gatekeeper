package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.Streak;
import com.smartappgatekeeper.database.entities.UsageEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * ViewModel for RoadmapFragment
 * Manages learning progress, achievements, and roadmap data
 */
public class RoadmapViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    private final MutableLiveData<List<LearningMilestone>> learningProgress = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentStreak = new MutableLiveData<>();
    private final MutableLiveData<List<Achievement>> achievements = new MutableLiveData<>();
    private final MutableLiveData<List<UsageEvent>> recentActivity = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public RoadmapViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        loadData();
    }
    
    /**
     * Load all roadmap data
     */
    private void loadData() {
        loadLearningProgress();
        loadCurrentStreak();
        loadAchievements();
        loadRecentActivity();
    }
    
    /**
     * Load learning progress milestones
     */
    private void loadLearningProgress() {
        List<LearningMilestone> milestones = new ArrayList<>();
        
        // Beginner milestones
        milestones.add(new LearningMilestone("First Quiz", "Complete your first quiz", 0, true));
        milestones.add(new LearningMilestone("3-Day Streak", "Maintain a 3-day learning streak", 3, false));
        milestones.add(new LearningMilestone("10 Questions", "Answer 10 questions correctly", 10, false));
        
        // Intermediate milestones
        milestones.add(new LearningMilestone("1 Week Streak", "Maintain a 7-day learning streak", 7, false));
        milestones.add(new LearningMilestone("50 Questions", "Answer 50 questions correctly", 50, false));
        milestones.add(new LearningMilestone("First Achievement", "Earn your first achievement", 1, false));
        
        // Advanced milestones
        milestones.add(new LearningMilestone("1 Month Streak", "Maintain a 30-day learning streak", 30, false));
        milestones.add(new LearningMilestone("100 Questions", "Answer 100 questions correctly", 100, false));
        milestones.add(new LearningMilestone("Master Level", "Reach master level in any category", 1, false));
        
        learningProgress.setValue(milestones);
    }
    
    /**
     * Load current streak data
     */
    private void loadCurrentStreak() {
        repository.getStreakByType("daily").observeForever(streak -> {
            if (streak != null) {
                currentStreak.setValue(streak.getCount());
            } else {
                currentStreak.setValue(0);
            }
        });
    }
    
    /**
     * Load achievements
     */
    private void loadAchievements() {
        List<Achievement> achievementList = new ArrayList<>();
        
        // Learning achievements
        achievementList.add(new Achievement("First Steps", "Complete your first quiz", "quiz", false));
        achievementList.add(new Achievement("Streak Master", "Maintain a 7-day streak", "streak", false));
        achievementList.add(new Achievement("Question Master", "Answer 50 questions correctly", "questions", false));
        achievementList.add(new Achievement("Time Saver", "Save 1 hour of screen time", "time", false));
        achievementList.add(new Achievement("Focus Master", "Complete 10 quizzes in a row", "focus", false));
        
        achievements.setValue(achievementList);
    }
    
    /**
     * Load recent activity
     */
    private void loadRecentActivity() {
        repository.getAllUsageEvents().observeForever(events -> {
            if (events != null && !events.isEmpty()) {
                // Get last 10 events
                List<UsageEvent> recent = events.size() > 10 ? 
                    events.subList(events.size() - 10, events.size()) : events;
                recentActivity.setValue(recent);
            } else {
                recentActivity.setValue(new ArrayList<>());
            }
        });
    }
    
    /**
     * Refresh all data
     */
    public void refreshData() {
        loadData();
    }
    
    // Getters
    public LiveData<List<LearningMilestone>> getLearningProgress() {
        return learningProgress;
    }
    
    public LiveData<Integer> getCurrentStreak() {
        return currentStreak;
    }
    
    public LiveData<List<Achievement>> getAchievements() {
        return achievements;
    }
    
    public LiveData<List<UsageEvent>> getRecentActivity() {
        return recentActivity;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Learning milestone data class
     */
    public static class LearningMilestone {
        private String title;
        private String description;
        private int targetValue;
        private boolean isCompleted;
        
        public LearningMilestone(String title, String description, int targetValue, boolean isCompleted) {
            this.title = title;
            this.description = description;
            this.targetValue = targetValue;
            this.isCompleted = isCompleted;
        }
        
        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getTargetValue() { return targetValue; }
        public void setTargetValue(int targetValue) { this.targetValue = targetValue; }
        
        public boolean isCompleted() { return isCompleted; }
        public void setCompleted(boolean completed) { isCompleted = completed; }
    }
    
    /**
     * Achievement data class
     */
    public static class Achievement {
        private String name;
        private String description;
        private String category;
        private boolean isUnlocked;
        
        public Achievement(String name, String description, String category, boolean isUnlocked) {
            this.name = name;
            this.description = description;
            this.category = category;
            this.isUnlocked = isUnlocked;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public boolean isUnlocked() { return isUnlocked; }
        public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
    }
}
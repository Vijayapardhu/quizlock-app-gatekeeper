package com.vijayapardhu.quizlock.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.RestrictedApp;
import com.vijayapardhu.quizlock.db.RestrictedAppDao;
import com.vijayapardhu.quizlock.db.UserPreferences;
import com.vijayapardhu.quizlock.db.UserPreferencesDao;
import com.vijayapardhu.quizlock.utils.AppUtils;
import com.vijayapardhu.quizlock.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnboardingViewModel extends AndroidViewModel {
    
    private final RestrictedAppDao restrictedAppDao;
    private final UserPreferencesDao userPreferencesDao;
    private final ExecutorService executor;
    
    private final MutableLiveData<List<AppUtils.InstalledApp>> installedApps = new MutableLiveData<>();
    private final MutableLiveData<List<String>> selectedApps = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> selectedTopics = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onboardingComplete = new MutableLiveData<>(false);
    
    private final List<String> availableTopics = Arrays.asList(
        Constants.TOPIC_MATH,
        Constants.TOPIC_PROGRAMMING,
        Constants.TOPIC_GENERAL_KNOWLEDGE,
        Constants.TOPIC_VOCABULARY,
        Constants.TOPIC_FITNESS,
        Constants.TOPIC_SCIENCE,
        Constants.TOPIC_HISTORY,
        Constants.TOPIC_GEOGRAPHY
    );
    
    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        QuizLockDatabase database = QuizLockDatabase.getDatabase(application);
        restrictedAppDao = database.restrictedAppDao();
        userPreferencesDao = database.userPreferencesDao();
        executor = Executors.newFixedThreadPool(2);
        
        loadInstalledApps();
        initializeSelectedTopics();
    }
    
    private void loadInstalledApps() {
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                List<AppUtils.InstalledApp> apps = AppUtils.getInstalledApps(getApplication());
                installedApps.postValue(apps);
            } catch (Exception e) {
                errorMessage.postValue("Failed to load installed apps: " + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
    }
    
    private void initializeSelectedTopics() {
        // Pre-select Math and General Knowledge
        List<String> defaultTopics = new ArrayList<>();
        defaultTopics.add(Constants.TOPIC_MATH);
        defaultTopics.add(Constants.TOPIC_GENERAL_KNOWLEDGE);
        selectedTopics.setValue(defaultTopics);
    }
    
    public void toggleAppSelection(String packageName) {
        List<String> current = selectedApps.getValue();
        if (current == null) current = new ArrayList<>();
        
        List<String> updated = new ArrayList<>(current);
        if (updated.contains(packageName)) {
            updated.remove(packageName);
        } else {
            updated.add(packageName);
        }
        selectedApps.setValue(updated);
    }
    
    public void toggleTopicSelection(String topic) {
        List<String> current = selectedTopics.getValue();
        if (current == null) current = new ArrayList<>();
        
        List<String> updated = new ArrayList<>(current);
        if (updated.contains(topic)) {
            updated.remove(topic);
        } else {
            updated.add(topic);
        }
        selectedTopics.setValue(updated);
    }
    
    public void completeOnboarding() {
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                // Save selected apps as restricted
                List<String> apps = selectedApps.getValue();
                if (apps != null) {
                    for (String packageName : apps) {
                        String appName = AppUtils.getAppName(getApplication(), packageName);
                        RestrictedApp restrictedApp = new RestrictedApp(
                            packageName, appName, "", true, Constants.DEFAULT_SESSION_MINUTES
                        );
                        restrictedAppDao.insertApp(restrictedApp);
                    }
                }
                
                // Save selected topics
                List<String> topics = selectedTopics.getValue();
                if (topics != null && !topics.isEmpty()) {
                    String topicsString = String.join(",", topics);
                    userPreferencesDao.insertPreference(
                        new UserPreferences(Constants.PREF_SELECTED_TOPICS, topicsString)
                    );
                }
                
                // Mark onboarding as completed
                userPreferencesDao.insertPreference(
                    new UserPreferences(Constants.PREF_ONBOARDING_COMPLETED, "true")
                );
                
                // Initialize other default preferences
                userPreferencesDao.insertPreference(
                    new UserPreferences(Constants.PREF_CURRENT_STREAK, "0")
                );
                userPreferencesDao.insertPreference(
                    new UserPreferences(Constants.PREF_BEST_STREAK, "0")
                );
                userPreferencesDao.insertPreference(
                    new UserPreferences(Constants.PREF_NOTIFICATIONS_ENABLED, "true")
                );
                userPreferencesDao.insertPreference(
                    new UserPreferences(Constants.PREF_DAILY_GOAL, "5")
                );
                
                onboardingComplete.postValue(true);
                
            } catch (Exception e) {
                errorMessage.postValue("Failed to complete onboarding: " + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
    }
    
    // Getters for LiveData
    public LiveData<List<AppUtils.InstalledApp>> getInstalledApps() { return installedApps; }
    public LiveData<List<String>> getSelectedApps() { return selectedApps; }
    public LiveData<List<String>> getSelectedTopics() { return selectedTopics; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getOnboardingComplete() { return onboardingComplete; }
    public List<String> getAvailableTopics() { return availableTopics; }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
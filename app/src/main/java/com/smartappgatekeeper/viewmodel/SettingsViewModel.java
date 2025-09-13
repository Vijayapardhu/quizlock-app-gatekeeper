package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.AppSettings;
import java.util.concurrent.CompletableFuture;

/**
 * ViewModel for SettingsFragment
 * Manages app settings and preferences
 */
public class SettingsViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    private final MutableLiveData<Boolean> darkModeEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> soundEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> vibrationEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notificationsEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> analyticsEnabled = new MutableLiveData<>();
    private final MutableLiveData<String> selectedLanguage = new MutableLiveData<>();
    private final MutableLiveData<Integer> quizDifficulty = new MutableLiveData<>();
    private final MutableLiveData<Integer> sessionDuration = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public SettingsViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        loadSettings();
    }
    
    /**
     * Load app settings
     */
    private void loadSettings() {
        repository.getAppSettings().observeForever(settings -> {
            if (settings != null) {
                darkModeEnabled.setValue(settings.isDarkModeEnabled());
                soundEnabled.setValue(settings.isSoundEnabled());
                vibrationEnabled.setValue(settings.isVibrationEnabled());
                notificationsEnabled.setValue(settings.isNotificationsEnabled());
                analyticsEnabled.setValue(settings.isAnalyticsEnabled());
                selectedLanguage.setValue(settings.getLanguage());
                quizDifficulty.setValue(0); // Default to 0 for now
                sessionDuration.setValue(settings.getSessionDuration());
            } else {
                // Default settings
                darkModeEnabled.setValue(false);
                soundEnabled.setValue(true);
                vibrationEnabled.setValue(true);
                notificationsEnabled.setValue(true);
                analyticsEnabled.setValue(true);
                selectedLanguage.setValue("en");
                quizDifficulty.setValue(1); // Easy
                sessionDuration.setValue(5); // 5 minutes
            }
        });
    }
    
    /**
     * Update dark mode setting
     */
    public void updateDarkMode(boolean enabled) {
        darkModeEnabled.setValue(enabled);
        updateSetting("darkModeEnabled", enabled);
    }
    
    /**
     * Update sound setting
     */
    public void updateSound(boolean enabled) {
        soundEnabled.setValue(enabled);
        updateSetting("soundEnabled", enabled);
    }
    
    /**
     * Update vibration setting
     */
    public void updateVibration(boolean enabled) {
        vibrationEnabled.setValue(enabled);
        updateSetting("vibrationEnabled", enabled);
    }
    
    /**
     * Update notifications setting
     */
    public void updateNotifications(boolean enabled) {
        notificationsEnabled.setValue(enabled);
        updateSetting("notificationsEnabled", enabled);
    }
    
    /**
     * Update analytics setting
     */
    public void updateAnalytics(boolean enabled) {
        analyticsEnabled.setValue(enabled);
        updateSetting("analyticsEnabled", enabled);
    }
    
    /**
     * Update language setting
     */
    public void updateLanguage(String language) {
        selectedLanguage.setValue(language);
        updateSetting("language", language);
    }
    
    /**
     * Update quiz difficulty setting
     */
    public void updateQuizDifficulty(int difficulty) {
        quizDifficulty.setValue(difficulty);
        updateSetting("quizDifficulty", difficulty);
    }
    
    /**
     * Update session duration setting
     */
    public void updateSessionDuration(int duration) {
        sessionDuration.setValue(duration);
        updateSetting("sessionDuration", duration);
    }
    
    /**
     * Update a setting in the database
     */
    private void updateSetting(String key, Object value) {
        CompletableFuture.runAsync(() -> {
            try {
                AppSettings settings = repository.getAppSettings().getValue();
                if (settings == null) {
                    settings = new AppSettings();
                }
                
                switch (key) {
                    case "darkModeEnabled":
                        settings.setDarkModeEnabled((Boolean) value);
                        break;
                    case "soundEnabled":
                        settings.setSoundEnabled((Boolean) value);
                        break;
                    case "vibrationEnabled":
                        settings.setVibrationEnabled((Boolean) value);
                        break;
                    case "notificationsEnabled":
                        settings.setNotificationsEnabled((Boolean) value);
                        break;
                    case "analyticsEnabled":
                        settings.setAnalyticsEnabled((Boolean) value);
                        break;
                    case "language":
                        settings.setLanguage((String) value);
                        break;
                    case "quizDifficulty":
                        settings.setQuizDifficulty("medium"); // Default value
                        break;
                    case "sessionDuration":
                        settings.setSessionDuration((Integer) value);
                        break;
                }
                
                repository.updateAppSettings(settings);
            } catch (Exception e) {
                errorMessage.postValue("Failed to update setting: " + e.getMessage());
            }
        });
    }
    
    /**
     * Reset all settings to default
     */
    public void resetToDefaults() {
        darkModeEnabled.setValue(false);
        soundEnabled.setValue(true);
        vibrationEnabled.setValue(true);
        notificationsEnabled.setValue(true);
        analyticsEnabled.setValue(true);
        selectedLanguage.setValue("en");
        quizDifficulty.setValue(1);
        sessionDuration.setValue(5);
        
        CompletableFuture.runAsync(() -> {
            try {
                AppSettings settings = new AppSettings();
                repository.updateAppSettings(settings);
            } catch (Exception e) {
                errorMessage.postValue("Failed to reset settings: " + e.getMessage());
            }
        });
    }
    
    /**
     * Export settings
     */
    public void exportSettings() {
        // Implementation for exporting settings to file
        errorMessage.setValue("Export feature coming soon!");
    }
    
    /**
     * Import settings
     */
    public void importSettings() {
        // Implementation for importing settings from file
        errorMessage.setValue("Import feature coming soon!");
    }
    
    // Getters
    public LiveData<Boolean> getDarkModeEnabled() {
        return darkModeEnabled;
    }
    
    public LiveData<Boolean> getSoundEnabled() {
        return soundEnabled;
    }
    
    public LiveData<Boolean> getVibrationEnabled() {
        return vibrationEnabled;
    }
    
    public LiveData<Boolean> getNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public LiveData<Boolean> getAnalyticsEnabled() {
        return analyticsEnabled;
    }
    
    public LiveData<String> getSelectedLanguage() {
        return selectedLanguage;
    }
    
    public LiveData<Integer> getQuizDifficulty() {
        return quizDifficulty;
    }
    
    public LiveData<Integer> getSessionDuration() {
        return sessionDuration;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
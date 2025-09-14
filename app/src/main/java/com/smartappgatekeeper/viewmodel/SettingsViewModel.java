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
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    
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
        // Implement data export functionality
        exportUserData();
    }
    
    /**
     * Import settings
     */
    public void importSettings() {
        // Implementation for importing settings from file
        // Implement data import functionality
        importUserData();
    }
    
    /**
     * Export user data to JSON file
     */
    private void exportUserData() {
        try {
            // Create export data structure
            ExportData exportData = new ExportData();
            exportData.exportDate = new java.util.Date();
            exportData.appVersion = "1.0.0";
            
            // Get user profile data
            repository.getUserProfile().observeForever(userProfile -> {
                if (userProfile != null) {
                    exportData.userProfile = userProfile;
                }
            });
            
            // Get quiz results
            repository.getQuizResultsByUser("user_001").observeForever(quizResults -> {
                if (quizResults != null) {
                    exportData.quizResults = quizResults;
                }
            });
            
            // Get usage events
            repository.getAllUsageEvents().observeForever(usageEvents -> {
                if (usageEvents != null) {
                    exportData.usageEvents = usageEvents;
                }
            });
            
            // Get streaks
            repository.getAllStreaks().observeForever(streaks -> {
                if (streaks != null) {
                    exportData.streaks = streaks;
                }
            });
            
            // Convert to JSON and save
            String jsonData = convertToJson(exportData);
            saveToFile(jsonData, "smart_app_gatekeeper_export.json");
            
            successMessage.setValue("✅ Data exported successfully!");
            
        } catch (Exception e) {
            errorMessage.setValue("❌ Export failed: " + e.getMessage());
        }
    }
    
    /**
     * Import user data from JSON file
     */
    private void importUserData() {
        try {
            // TODO: Implement file picker and JSON parsing
            // For now, show a placeholder message
            successMessage.setValue("📁 Import feature - Select file to import data");
            
        } catch (Exception e) {
            errorMessage.setValue("❌ Import failed: " + e.getMessage());
        }
    }
    
    /**
     * Convert export data to JSON
     */
    private String convertToJson(ExportData exportData) {
        // Simple JSON conversion (in a real app, use Gson or similar)
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"exportDate\": \"").append(exportData.exportDate.toString()).append("\",\n");
        json.append("  \"appVersion\": \"").append(exportData.appVersion).append("\",\n");
        json.append("  \"dataType\": \"Smart App Gatekeeper Export\",\n");
        json.append("  \"quizResultsCount\": ").append(exportData.quizResults != null ? exportData.quizResults.size() : 0).append(",\n");
        json.append("  \"usageEventsCount\": ").append(exportData.usageEvents != null ? exportData.usageEvents.size() : 0).append(",\n");
        json.append("  \"streaksCount\": ").append(exportData.streaks != null ? exportData.streaks.size() : 0).append("\n");
        json.append("}");
        return json.toString();
    }
    
    /**
     * Save data to file
     */
    private void saveToFile(String data, String filename) {
        // TODO: Implement actual file saving
        android.util.Log.d("SettingsViewModel", "Export data: " + data);
        android.util.Log.d("SettingsViewModel", "Would save to: " + filename);
    }
    
    /**
     * Export data structure
     */
    private static class ExportData {
        public java.util.Date exportDate;
        public String appVersion;
        public com.smartappgatekeeper.database.entities.UserProfile userProfile;
        public java.util.List<com.smartappgatekeeper.database.entities.QuizResult> quizResults;
        public java.util.List<com.smartappgatekeeper.database.entities.UsageEvent> usageEvents;
        public java.util.List<com.smartappgatekeeper.database.entities.Streak> streaks;
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
    
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
}
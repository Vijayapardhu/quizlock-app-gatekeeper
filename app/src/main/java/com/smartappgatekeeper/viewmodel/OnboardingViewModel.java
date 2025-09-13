package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.UserProfile;
import com.smartappgatekeeper.database.entities.TargetApp;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * ViewModel for OnboardingActivity
 * Manages user onboarding flow and initial setup
 */
public class OnboardingViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    private final MutableLiveData<Integer> currentStep = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<Integer> userAge = new MutableLiveData<>();
    private final MutableLiveData<String> selectedGoal = new MutableLiveData<>();
    private final MutableLiveData<List<TargetApp>> selectedApps = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOnboardingComplete = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public OnboardingViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        currentStep.setValue(0);
        selectedApps.setValue(new ArrayList<>());
        isOnboardingComplete.setValue(false);
        loadAvailableApps();
    }
    
    /**
     * Load available target apps
     */
    private void loadAvailableApps() {
        List<TargetApp> apps = new ArrayList<>();
        
        // Popular social media apps
        apps.add(new TargetApp("com.facebook.katana", "Facebook", "Social Media", false));
        apps.add(new TargetApp("com.instagram.android", "Instagram", "Social Media", false));
        apps.add(new TargetApp("com.twitter.android", "Twitter", "Social Media", false));
        apps.add(new TargetApp("com.snapchat.android", "Snapchat", "Social Media", false));
        apps.add(new TargetApp("com.whatsapp", "WhatsApp", "Messaging", false));
        apps.add(new TargetApp("com.telegram.messenger", "Telegram", "Messaging", false));
        
        // Entertainment apps
        apps.add(new TargetApp("com.netflix.mediaclient", "Netflix", "Entertainment", false));
        apps.add(new TargetApp("com.youtube", "YouTube", "Entertainment", false));
        apps.add(new TargetApp("com.spotify.music", "Spotify", "Music", false));
        apps.add(new TargetApp("com.tiktok", "TikTok", "Entertainment", false));
        
        // Gaming apps
        apps.add(new TargetApp("com.supercell.clashofclans", "Clash of Clans", "Gaming", false));
        apps.add(new TargetApp("com.mojang.minecraftpe", "Minecraft", "Gaming", false));
        apps.add(new TargetApp("com.king.candycrushsaga", "Candy Crush", "Gaming", false));
        
        // News and browsing
        apps.add(new TargetApp("com.android.chrome", "Chrome", "Browser", false));
        apps.add(new TargetApp("com.reddit.frontpage", "Reddit", "Social", false));
        apps.add(new TargetApp("com.pinterest", "Pinterest", "Social", false));
        
        selectedApps.setValue(apps);
    }
    
    /**
     * Move to next step
     */
    public void nextStep() {
        Integer current = currentStep.getValue();
        if (current != null && current < 4) {
            currentStep.setValue(current + 1);
        }
    }
    
    /**
     * Move to previous step
     */
    public void previousStep() {
        Integer current = currentStep.getValue();
        if (current != null && current > 0) {
            currentStep.setValue(current - 1);
        }
    }
    
    /**
     * Set user name
     */
    public void setUserName(String name) {
        userName.setValue(name);
    }
    
    /**
     * Set user email
     */
    public void setUserEmail(String email) {
        userEmail.setValue(email);
    }
    
    /**
     * Set user age
     */
    public void setUserAge(int age) {
        userAge.setValue(age);
    }
    
    /**
     * Set selected goal
     */
    public void setSelectedGoal(String goal) {
        selectedGoal.setValue(goal);
    }
    
    /**
     * Toggle app selection
     */
    public void toggleAppSelection(String packageName) {
        List<TargetApp> apps = selectedApps.getValue();
        if (apps != null) {
            for (TargetApp app : apps) {
                if (app.getPackageName().equals(packageName)) {
                    app.setSelected(!app.isSelected());
                    break;
                }
            }
            selectedApps.setValue(apps);
        }
    }
    
    /**
     * Complete onboarding
     */
    public void completeOnboarding() {
        CompletableFuture.runAsync(() -> {
            try {
                // Create user profile
                UserProfile profile = new UserProfile();
                profile.setName(userName.getValue());
                profile.setEmail(userEmail.getValue());
                profile.setAge(userAge.getValue());
                profile.setGoal(selectedGoal.getValue());
                profile.setJoinDate(System.currentTimeMillis());
                
                repository.insertUserProfile(profile);
                
                // Save selected target apps
                List<TargetApp> apps = selectedApps.getValue();
                if (apps != null) {
                    for (TargetApp app : apps) {
                        if (app.isSelected()) {
                            repository.insertTargetApp(app);
                        }
                    }
                }
                
                // Mark onboarding as complete
                isOnboardingComplete.postValue(true);
                
            } catch (Exception e) {
                errorMessage.postValue("Failed to complete onboarding: " + e.getMessage());
            }
        });
    }
    
    /**
     * Skip onboarding
     */
    public void skipOnboarding() {
        CompletableFuture.runAsync(() -> {
            try {
                // Create default user profile
                UserProfile profile = new UserProfile();
                profile.setName("User");
                profile.setEmail("");
                profile.setAge(25);
                profile.setGoal("Reduce screen time");
                profile.setJoinDate(System.currentTimeMillis());
                
                repository.insertUserProfile(profile);
                isOnboardingComplete.postValue(true);
                
            } catch (Exception e) {
                errorMessage.postValue("Failed to skip onboarding: " + e.getMessage());
            }
        });
    }
    
    /**
     * Check if current step is valid
     */
    public boolean isCurrentStepValid() {
        Integer current = currentStep.getValue();
        if (current == null) return false;
        
        switch (current) {
            case 0: // Welcome step
                return true;
            case 1: // User info step
                return userName.getValue() != null && !userName.getValue().trim().isEmpty();
            case 2: // Goal selection step
                return selectedGoal.getValue() != null && !selectedGoal.getValue().trim().isEmpty();
            case 3: // App selection step
                List<TargetApp> apps = selectedApps.getValue();
                if (apps == null) return false;
                return apps.stream().anyMatch(TargetApp::isSelected);
            case 4: // Completion step
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Get available goals
     */
    public List<String> getAvailableGoals() {
        List<String> goals = new ArrayList<>();
        goals.add("Reduce screen time");
        goals.add("Improve focus");
        goals.add("Learn new skills");
        goals.add("Break social media addiction");
        goals.add("Increase productivity");
        goals.add("Better work-life balance");
        return goals;
    }
    
    // Getters
    public LiveData<Integer> getCurrentStep() {
        return currentStep;
    }
    
    public LiveData<String> getUserName() {
        return userName;
    }
    
    public LiveData<String> getUserEmail() {
        return userEmail;
    }
    
    public LiveData<Integer> getUserAge() {
        return userAge;
    }
    
    public LiveData<String> getSelectedGoal() {
        return selectedGoal;
    }
    
    public LiveData<List<TargetApp>> getSelectedApps() {
        return selectedApps;
    }
    
    public LiveData<Boolean> getIsOnboardingComplete() {
        return isOnboardingComplete;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}

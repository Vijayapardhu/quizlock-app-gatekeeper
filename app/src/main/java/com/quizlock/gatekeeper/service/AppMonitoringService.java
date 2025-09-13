package com.quizlock.gatekeeper.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.quizlock.gatekeeper.QuizLockApplication;
import com.quizlock.gatekeeper.data.repository.QuizRepository;
import com.quizlock.gatekeeper.ui.quiz.QuizActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Accessibility service for monitoring app launches and blocking restricted apps
 */
public class AppMonitoringService extends AccessibilityService {
    
    private static final String TAG = "AppMonitoringService";
    
    private QuizRepository repository;
    private Handler mainHandler;
    private Set<String> blockedPackages;
    private String lastLaunchedPackage = "";
    private long lastLaunchTime = 0;
    
    // Prevent rapid consecutive triggers
    private static final long LAUNCH_DEBOUNCE_MS = 2000;
    
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "AppMonitoringService connected");
        
        initializeService();
        loadBlockedApps();
    }
    
    private void initializeService() {
        QuizLockApplication app = (QuizLockApplication) getApplication();
        repository = app.getQuizRepository();
        mainHandler = new Handler(Looper.getMainLooper());
        blockedPackages = new HashSet<>();
    }
    
    private void loadBlockedApps() {
        if (repository != null) {
            repository.getBlockedApps().observeForever(blockedApps -> {
                blockedPackages.clear();
                if (blockedApps != null) {
                    for (com.quizlock.gatekeeper.data.model.AppUsage app : blockedApps) {
                        if (app.isBlocked() && !app.isCurrentlyUnlocked()) {
                            blockedPackages.add(app.getPackageName());
                        }
                    }
                }
                Log.d(TAG, "Loaded " + blockedPackages.size() + " blocked apps");
            });
        }
    }
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }
        
        ComponentName componentName = new ComponentName(
            event.getPackageName().toString(),
            event.getClassName().toString()
        );
        
        String packageName = componentName.getPackageName();
        
        // Skip system apps and our own app
        if (isSystemPackage(packageName) || packageName.equals(getPackageName())) {
            return;
        }
        
        // Debounce rapid launches
        long currentTime = System.currentTimeMillis();
        if (packageName.equals(lastLaunchedPackage) && 
            (currentTime - lastLaunchTime) < LAUNCH_DEBOUNCE_MS) {
            return;
        }
        
        lastLaunchedPackage = packageName;
        lastLaunchTime = currentTime;
        
        Log.d(TAG, "App launched: " + packageName);
        
        // Check if this app is blocked
        if (blockedPackages.contains(packageName)) {
            Log.d(TAG, "Blocked app detected: " + packageName);
            handleBlockedAppLaunch(packageName);
        }
        
        // Record app launch
        if (repository != null) {
            repository.recordAppLaunch(packageName);
        }
    }
    
    private void handleBlockedAppLaunch(String packageName) {
        // Record blocked attempt
        if (repository != null) {
            repository.recordBlockedAttempt(packageName);
        }
        
        // Get app name for display
        String appName = getAppName(packageName);
        
        // Launch quiz activity
        Intent quizIntent = new Intent(this, QuizActivity.class);
        quizIntent.putExtra(QuizActivity.EXTRA_PACKAGE_NAME, packageName);
        quizIntent.putExtra(QuizActivity.EXTRA_APP_NAME, appName);
        quizIntent.putExtra(QuizActivity.EXTRA_CATEGORY, getPreferredCategory());
        quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        // Start quiz activity after a small delay to ensure proper app launch detection
        mainHandler.postDelayed(() -> {
            try {
                startActivity(quizIntent);
                
                // Try to close the blocked app (bring user back to home)
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                
            } catch (Exception e) {
                Log.e(TAG, "Error launching quiz activity", e);
            }
        }, 500);
    }
    
    private String getAppName(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            return pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }
    
    private String getPreferredCategory() {
        QuizLockApplication app = (QuizLockApplication) getApplication();
        return app.getPreferencesManager().getPreferredCategory();
    }
    
    private boolean isSystemPackage(String packageName) {
        if (packageName == null) return true;
        
        // Common system packages to ignore
        return packageName.startsWith("com.android.") ||
               packageName.startsWith("android.") ||
               packageName.equals("com.google.android.inputmethod.latin") ||
               packageName.equals("com.android.systemui") ||
               packageName.equals("com.android.launcher") ||
               packageName.equals("com.sec.android.app.launcher") ||
               packageName.equals("com.miui.home");
    }
    
    @Override
    public void onInterrupt() {
        Log.d(TAG, "AppMonitoringService interrupted");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AppMonitoringService destroyed");
    }
    
    /**
     * Check if this app is currently unlocked
     */
    private void checkAppUnlockStatus(String packageName, Runnable onBlocked) {
        if (repository != null) {
            repository.getAppUsage(packageName, appUsage -> {
                if (appUsage != null && appUsage.isCurrentlyUnlocked()) {
                    Log.d(TAG, "App " + packageName + " is currently unlocked");
                    // App is unlocked, allow access
                } else {
                    Log.d(TAG, "App " + packageName + " is blocked");
                    mainHandler.post(onBlocked);
                }
            });
        } else {
            mainHandler.post(onBlocked);
        }
    }
}
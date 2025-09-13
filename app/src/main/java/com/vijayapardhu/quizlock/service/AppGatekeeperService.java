package com.vijayapardhu.quizlock.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.RestrictedApp;
import com.vijayapardhu.quizlock.db.RestrictedAppDao;
import com.vijayapardhu.quizlock.ui.QuizInterceptActivity;
import com.vijayapardhu.quizlock.utils.AppUtils;
import com.vijayapardhu.quizlock.utils.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppGatekeeperService extends AccessibilityService {
    
    private static final String TAG = "AppGatekeeperService";
    
    private RestrictedAppDao restrictedAppDao;
    private ExecutorService executor;
    private String lastPackageName = "";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        
        QuizLockDatabase database = QuizLockDatabase.getDatabase(this);
        restrictedAppDao = database.restrictedAppDao();
        executor = Executors.newSingleThreadExecutor();
    }
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? 
                event.getPackageName().toString() : null;
            
            if (packageName != null && !packageName.equals(lastPackageName)) {
                lastPackageName = packageName;
                checkAndInterceptApp(packageName);
            }
        }
    }
    
    private void checkAndInterceptApp(String packageName) {
        // Don't intercept our own app or system apps
        if (packageName.equals(getPackageName()) || 
            packageName.startsWith("com.android.") ||
            packageName.startsWith("android.")) {
            return;
        }
        
        executor.execute(() -> {
            try {
                RestrictedApp restrictedApp = restrictedAppDao.getAppByPackageName(packageName);
                
                if (restrictedApp != null && restrictedApp.isRestricted()) {
                    Log.d(TAG, "Intercepting restricted app: " + packageName);
                    
                    // Check if daily limit is exceeded
                    if (isDailyLimitExceeded(restrictedApp)) {
                        showQuizIntercept(packageName, restrictedApp.getAppName());
                    } else {
                        // Check if there's an active session
                        if (!hasActiveSession(packageName)) {
                            showQuizIntercept(packageName, restrictedApp.getAppName());
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking restricted app", e);
            }
        });
    }
    
    private boolean isDailyLimitExceeded(RestrictedApp app) {
        // Check if it's a new day and reset if needed
        if (AppUtils.isNewDay(app.getLastResetTimestamp())) {
            app.setUsedTimeMinutes(0);
            app.setLastResetTimestamp(System.currentTimeMillis());
            restrictedAppDao.updateApp(app);
            return false;
        }
        
        return app.getUsedTimeMinutes() >= app.getDailyLimitMinutes();
    }
    
    private boolean hasActiveSession(String packageName) {
        // This would check if there's an active timer session for this app
        // For now, always return false to trigger quiz
        return false;
    }
    
    private void showQuizIntercept(String packageName, String appName) {
        Intent intent = new Intent(this, QuizInterceptActivity.class);
        intent.putExtra(Constants.EXTRA_PACKAGE_NAME, packageName);
        intent.putExtra(Constants.EXTRA_APP_NAME, appName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                       Intent.FLAG_ACTIVITY_CLEAR_TOP |
                       Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        startActivity(intent);
        
        // Return to home screen to prevent access to the restricted app
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }
    
    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service interrupted");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        if (executor != null) {
            executor.shutdown();
        }
    }
    
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "Service connected");
    }
}
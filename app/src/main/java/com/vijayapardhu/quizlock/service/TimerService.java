package com.vijayapardhu.quizlock.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vijayapardhu.quizlock.db.AppSession;
import com.vijayapardhu.quizlock.db.AppSessionDao;
import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.RestrictedApp;
import com.vijayapardhu.quizlock.db.RestrictedAppDao;
import com.vijayapardhu.quizlock.utils.AppUtils;
import com.vijayapardhu.quizlock.utils.Constants;
import com.vijayapardhu.quizlock.utils.NotificationUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimerService extends Service {
    
    private static final String TAG = "TimerService";
    private static final int NOTIFICATION_ID = 1001;
    
    private final IBinder binder = new TimerBinder();
    private CountDownTimer countDownTimer;
    private ExecutorService executor;
    
    private AppSessionDao appSessionDao;
    private RestrictedAppDao restrictedAppDao;
    
    private String currentPackageName;
    private String currentAppName;
    private long sessionId;
    private int remainingMinutes;
    private boolean isRunning = false;
    
    private TimerListener timerListener;
    
    public interface TimerListener {
        void onTimerUpdate(int remainingMinutes);
        void onTimerFinished();
    }
    
    public class TimerBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Timer service created");
        
        QuizLockDatabase database = QuizLockDatabase.getDatabase(this);
        appSessionDao = database.appSessionDao();
        restrictedAppDao = database.restrictedAppDao();
        executor = Executors.newSingleThreadExecutor();
        
        NotificationUtils.createNotificationChannels(this);
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    public void startTimer(String packageName, String appName, int durationMinutes, 
                          boolean wasQuizAnswered, boolean wasEmergencyUnlock) {
        if (isRunning) {
            stopTimer();
        }
        
        this.currentPackageName = packageName;
        this.currentAppName = appName;
        this.remainingMinutes = durationMinutes;
        this.isRunning = true;
        
        // Create session in database
        executor.execute(() -> {
            AppSession session = new AppSession(
                packageName, 
                System.currentTimeMillis(), 
                wasQuizAnswered, 
                wasEmergencyUnlock,
                AppUtils.getCurrentDate()
            );
            sessionId = appSessionDao.insertSession(session);
        });
        
        // Start countdown timer
        countDownTimer = new CountDownTimer(durationMinutes * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMinutes = (int) (millisUntilFinished / (1000 * 60)) + 1;
                
                // Update notification
                NotificationUtils.showSessionActiveNotification(
                    TimerService.this, currentAppName, remainingMinutes
                );
                
                // Notify listener
                if (timerListener != null) {
                    timerListener.onTimerUpdate(remainingMinutes);
                }
            }
            
            @Override
            public void onFinish() {
                endSession();
            }
        };
        
        countDownTimer.start();
        
        // Start foreground service
        Notification notification = NotificationUtils.createSessionNotification(
            this, appName, durationMinutes
        );
        startForeground(NOTIFICATION_ID, notification);
        
        Log.d(TAG, "Timer started for " + appName + " - " + durationMinutes + " minutes");
    }
    
    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        
        endSession();
    }
    
    private void endSession() {
        isRunning = false;
        
        // Update session in database
        executor.execute(() -> {
            AppSession session = appSessionDao.getActiveSession();
            if (session != null && session.getId() == sessionId) {
                session.endSession();
                appSessionDao.updateSession(session);
                
                // Update restricted app usage
                RestrictedApp restrictedApp = restrictedAppDao.getAppByPackageName(currentPackageName);
                if (restrictedApp != null) {
                    int newUsedTime = restrictedApp.getUsedTimeMinutes() + session.getDurationMinutes();
                    restrictedAppDao.updateUsedTime(currentPackageName, newUsedTime);
                }
            }
        });
        
        // Show session ended notification
        NotificationUtils.showSessionEndedNotification(this, currentAppName);
        
        // Cancel active session notification
        NotificationUtils.cancelSessionNotification(this);
        
        // Notify listener
        if (timerListener != null) {
            timerListener.onTimerFinished();
        }
        
        // Stop foreground service
        stopForeground(true);
        
        Log.d(TAG, "Session ended for " + currentAppName);
    }
    
    public void extendSession(int additionalMinutes) {
        if (isRunning && countDownTimer != null) {
            countDownTimer.cancel();
            
            int newDuration = remainingMinutes + additionalMinutes;
            startTimer(currentPackageName, currentAppName, newDuration, true, false);
        }
    }
    
    public boolean isTimerRunning() {
        return isRunning;
    }
    
    public int getRemainingMinutes() {
        return remainingMinutes;
    }
    
    public String getCurrentAppName() {
        return currentAppName;
    }
    
    public void setTimerListener(TimerListener listener) {
        this.timerListener = listener;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Timer service destroyed");
        
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        if (executor != null) {
            executor.shutdown();
        }
        
        NotificationUtils.cancelSessionNotification(this);
    }
}
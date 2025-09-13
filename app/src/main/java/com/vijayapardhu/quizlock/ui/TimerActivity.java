package com.vijayapardhu.quizlock.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.vijayapardhu.quizlock.databinding.ActivityTimerBinding;
import com.vijayapardhu.quizlock.service.TimerService;
import com.vijayapardhu.quizlock.utils.Constants;

public class TimerActivity extends AppCompatActivity implements TimerService.TimerListener {
    
    private ActivityTimerBinding binding;
    private TimerService timerService;
    private boolean isServiceBound = false;
    
    private String packageName;
    private String appName;
    private boolean wasEmergencyUnlock;
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.TimerBinder binder = (TimerService.TimerBinder) service;
            timerService = binder.getService();
            timerService.setTimerListener(TimerActivity.this);
            isServiceBound = true;
            
            // Start the timer session
            startTimerSession();
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
            timerService = null;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Get intent data
        packageName = getIntent().getStringExtra(Constants.EXTRA_PACKAGE_NAME);
        appName = getIntent().getStringExtra(Constants.EXTRA_APP_NAME);
        wasEmergencyUnlock = getIntent().getBooleanExtra("emergency_unlock", false);
        
        if (packageName == null) {
            finish();
            return;
        }
        
        setupUI();
        bindTimerService();
    }
    
    private void setupUI() {
        binding.tvAppName.setText(appName);
        
        binding.btnExtendSession.setOnClickListener(v -> {
            if (timerService != null) {
                timerService.extendSession(15); // Extend by 15 minutes
            }
        });
        
        binding.btnEndSession.setOnClickListener(v -> {
            if (timerService != null) {
                timerService.stopTimer();
            }
            finish();
        });
    }
    
    private void bindTimerService() {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent); // Start service first
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    private void startTimerSession() {
        if (timerService != null) {
            timerService.startTimer(
                packageName, 
                appName, 
                Constants.DEFAULT_SESSION_MINUTES,
                !wasEmergencyUnlock, // wasQuizAnswered
                wasEmergencyUnlock   // wasEmergencyUnlock
            );
        }
    }
    
    @Override
    public void onTimerUpdate(int remainingMinutes) {
        runOnUiThread(() -> {
            // Update timer display
            int minutes = remainingMinutes;
            int seconds = 0; // For simplicity, we're only showing minutes
            
            String timeText = String.format("%02d:%02d", minutes, seconds);
            binding.tvTimeRemaining.setText(timeText);
            
            // Update progress (assuming 30 minutes is 100%)
            int progress = (remainingMinutes * 100) / Constants.DEFAULT_SESSION_MINUTES;
            binding.progressTimer.setProgress(progress);
            
            // Change color based on remaining time
            if (remainingMinutes <= 5) {
                binding.progressTimer.setIndicatorColor(getColor(com.vijayapardhu.quizlock.R.color.timer_danger));
                binding.tvTimeRemaining.setTextColor(getColor(com.vijayapardhu.quizlock.R.color.timer_danger));
            } else if (remainingMinutes <= 10) {
                binding.progressTimer.setIndicatorColor(getColor(com.vijayapardhu.quizlock.R.color.timer_warning));
                binding.tvTimeRemaining.setTextColor(getColor(com.vijayapardhu.quizlock.R.color.timer_warning));
            }
        });
    }
    
    @Override
    public void onTimerFinished() {
        runOnUiThread(() -> {
            android.widget.Toast.makeText(this, "Session ended", android.widget.Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            if (timerService != null) {
                timerService.setTimerListener(null);
            }
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }
    
    @Override
    public void onBackPressed() {
        // Minimize the app instead of closing the timer
        moveTaskToBack(true);
    }
}
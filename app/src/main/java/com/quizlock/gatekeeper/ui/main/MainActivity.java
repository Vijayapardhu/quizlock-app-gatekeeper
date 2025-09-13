package com.quizlock.gatekeeper.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.quizlock.gatekeeper.R;
import com.quizlock.gatekeeper.QuizLockApplication;
import com.quizlock.gatekeeper.data.model.AppUsage;
import com.quizlock.gatekeeper.data.model.QuizHistory;
import com.quizlock.gatekeeper.ui.appselection.AppSelectionActivity;
import com.quizlock.gatekeeper.ui.settings.SettingsActivity;
import com.quizlock.gatekeeper.utils.PermissionHelper;
import com.quizlock.gatekeeper.viewmodel.MainViewModel;
import com.quizlock.gatekeeper.viewmodel.MainViewModelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity displaying dashboard with statistics and blocked apps
 */
public class MainActivity extends AppCompatActivity {
    
    private MainViewModel viewModel;
    
    // Views
    private TextView tvCurrentStreak;
    private TextView tvAccuracyRate;
    private TextView tvBlockedCount;
    private RecyclerView rvBlockedApps;
    private RecyclerView rvRecentActivity;
    private MaterialButton btnSelectApps;
    private MaterialButton btnSettings;
    
    // Adapters
    private BlockedAppsAdapter blockedAppsAdapter;
    private QuizHistoryAdapter historyAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViewModel();
        initializeViews();
        setupRecyclerViews();
        setupClickListeners();
        observeData();
        checkPermissions();
    }
    
    private void initializeViewModel() {
        QuizLockApplication app = (QuizLockApplication) getApplication();
        MainViewModelFactory factory = new MainViewModelFactory(app.getQuizRepository());
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
    }
    
    private void initializeViews() {
        tvCurrentStreak = findViewById(R.id.tv_current_streak);
        tvAccuracyRate = findViewById(R.id.tv_accuracy_rate);
        tvBlockedCount = findViewById(R.id.tv_blocked_count);
        rvBlockedApps = findViewById(R.id.rv_blocked_apps);
        rvRecentActivity = findViewById(R.id.rv_recent_activity);
        btnSelectApps = findViewById(R.id.btn_select_apps);
        btnSettings = findViewById(R.id.btn_settings);
    }
    
    private void setupRecyclerViews() {
        // Blocked apps RecyclerView
        blockedAppsAdapter = new BlockedAppsAdapter(new ArrayList<>());
        rvBlockedApps.setLayoutManager(new LinearLayoutManager(this));
        rvBlockedApps.setAdapter(blockedAppsAdapter);
        rvBlockedApps.setNestedScrollingEnabled(false);
        
        // Recent activity RecyclerView
        historyAdapter = new QuizHistoryAdapter(new ArrayList<>());
        rvRecentActivity.setLayoutManager(new LinearLayoutManager(this));
        rvRecentActivity.setAdapter(historyAdapter);
        rvRecentActivity.setNestedScrollingEnabled(false);
    }
    
    private void setupClickListeners() {
        btnSelectApps.setOnClickListener(v -> {
            if (PermissionHelper.hasAccessibilityPermission(this)) {
                startActivity(new Intent(this, AppSelectionActivity.class));
            } else {
                showPermissionDialog();
            }
        });
        
        btnSettings.setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class))
        );
    }
    
    private void observeData() {
        // Observe user statistics
        viewModel.getUserStats().observe(this, userStats -> {
            if (userStats != null) {
                tvCurrentStreak.setText(String.valueOf(userStats.getCurrentStreak()));
                tvAccuracyRate.setText(String.format("%.1f%%", userStats.getAccuracyPercentage()));
            }
        });
        
        // Observe blocked apps
        viewModel.getBlockedApps().observe(this, blockedApps -> {
            if (blockedApps != null) {
                tvBlockedCount.setText(String.valueOf(blockedApps.size()));
                blockedAppsAdapter.updateData(blockedApps);
            }
        });
        
        // Observe recent quiz history
        viewModel.getRecentHistory().observe(this, history -> {
            if (history != null) {
                historyAdapter.updateData(history);
            }
        });
    }
    
    private void checkPermissions() {
        if (!PermissionHelper.hasAccessibilityPermission(this)) {
            // Show info about enabling accessibility service
            Toast.makeText(this, R.string.service_required, Toast.LENGTH_LONG).show();
        }
    }
    
    private void showPermissionDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(R.string.permission_accessibility_title)
            .setMessage(R.string.permission_accessibility_message)
            .setPositiveButton(R.string.grant_permission, (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to the activity
        viewModel.refreshData();
    }
}
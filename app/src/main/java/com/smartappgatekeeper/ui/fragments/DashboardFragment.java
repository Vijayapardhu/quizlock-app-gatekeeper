package com.smartappgatekeeper.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.viewmodel.DashboardViewModel;
import com.smartappgatekeeper.ui.adapters.RecentActivityAdapter;

/**
 * Dashboard Fragment - Main screen showing usage statistics and progress
 * FR-016: System shall display daily and weekly usage statistics
 * FR-017: System shall show learning progress and streaks
 * FR-018: System shall display earned coins and achievements
 * FR-019: System shall provide visual charts and graphs
 * FR-020: System shall show recent activity history
 */
public class DashboardFragment extends Fragment {
    
    private DashboardViewModel viewModel;
    private RecentActivityAdapter recentActivityAdapter;
    
    // UI Views
    private TextView textTodayUsage;
    private TextView textWeeklyUsage;
    private TextView textCurrentStreak;
    private TextView textQuestionsAnswered;
    private TextView textAccuracyRate;
    private TextView textCoinsEarned;
    private TextView textCoursesCompleted;
    private TextView textError;
    
    private ProgressBar progressTodayUsage;
    private ProgressBar progressWeeklyUsage;
    private ProgressBar progressStreak;
    private ProgressBar progressAccuracy;
    
    private Button buttonRefresh;
    private Button buttonViewDetails;
    
    private LinearLayout layoutStats;
    private LinearLayout layoutProgress;
    private LinearLayout layoutError;
    
    private RecyclerView recyclerRecentActivity;
    private SwipeRefreshLayout swipeRefresh;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        initializeViews(view);
        
        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        
        // Setup UI
        setupUI();
        
        // Observe ViewModel
        observeViewModel();
        
        // Load data
        viewModel.loadDashboardData();
    }
    
    /**
     * Initialize all UI views
     */
    private void initializeViews(View view) {
        // Text views
        textTodayUsage = view.findViewById(R.id.text_today_usage);
        textWeeklyUsage = view.findViewById(R.id.text_weekly_usage);
        textCurrentStreak = view.findViewById(R.id.text_current_streak);
        textQuestionsAnswered = view.findViewById(R.id.text_questions_answered);
        textAccuracyRate = view.findViewById(R.id.text_accuracy_rate);
        textCoinsEarned = view.findViewById(R.id.text_coins_earned);
        textCoursesCompleted = view.findViewById(R.id.text_courses_completed);
        textError = view.findViewById(R.id.text_error);
        
        // Progress bars
        progressTodayUsage = view.findViewById(R.id.progress_today_usage);
        progressWeeklyUsage = view.findViewById(R.id.progress_weekly_usage);
        progressStreak = view.findViewById(R.id.progress_streak);
        progressAccuracy = view.findViewById(R.id.progress_accuracy);
        
        // Buttons
        buttonRefresh = view.findViewById(R.id.button_refresh);
        buttonViewDetails = view.findViewById(R.id.button_view_details);
        
        // Layouts
        layoutStats = view.findViewById(R.id.layout_stats);
        layoutProgress = view.findViewById(R.id.layout_progress);
        layoutError = view.findViewById(R.id.layout_error);
        
        // RecyclerView
        recyclerRecentActivity = view.findViewById(R.id.recycler_recent_activity);
        
        // SwipeRefreshLayout
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
    }
    
    /**
     * Setup UI components
     */
    private void setupUI() {
        // Setup RecyclerView
        recentActivityAdapter = new RecentActivityAdapter();
        recyclerRecentActivity.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRecentActivity.setAdapter(recentActivityAdapter);
        
        // Setup SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadDashboardData();
            swipeRefresh.setRefreshing(false);
        });
        
        // Setup button click listeners
        buttonRefresh.setOnClickListener(v -> viewModel.loadDashboardData());
        buttonViewDetails.setOnClickListener(v -> {
            // TODO: Navigate to detailed analytics
        });
    }
    
    /**
     * Observe ViewModel LiveData
     */
    private void observeViewModel() {
        // Today's usage
        viewModel.getTodayUsage().observe(getViewLifecycleOwner(), usage -> {
            if (textTodayUsage != null) {
                textTodayUsage.setText(usage);
            }
        });
        
        // Weekly usage
        viewModel.getWeeklyUsage().observe(getViewLifecycleOwner(), usage -> {
            if (textWeeklyUsage != null) {
                textWeeklyUsage.setText(usage);
            }
        });
        
        // Current streak
        viewModel.getCurrentStreak().observe(getViewLifecycleOwner(), streak -> {
            if (textCurrentStreak != null) {
                textCurrentStreak.setText(String.valueOf(streak));
            }
        });
        
        // Questions answered
        viewModel.getQuestionsAnswered().observe(getViewLifecycleOwner(), questions -> {
            if (textQuestionsAnswered != null) {
                textQuestionsAnswered.setText(String.valueOf(questions));
            }
        });
        
        // Accuracy rate
        viewModel.getAccuracyRate().observe(getViewLifecycleOwner(), accuracy -> {
            if (textAccuracyRate != null) {
                textAccuracyRate.setText(String.format("%.1f%%", accuracy));
            }
            if (progressAccuracy != null) {
                progressAccuracy.setProgress((int) Math.round(accuracy));
            }
        });
        
        // Coins earned
        viewModel.getCoinsEarned().observe(getViewLifecycleOwner(), coins -> {
            if (textCoinsEarned != null) {
                textCoinsEarned.setText(String.valueOf(coins));
            }
        });
        
        // Courses completed
        viewModel.getCoursesCompleted().observe(getViewLifecycleOwner(), courses -> {
            if (textCoursesCompleted != null) {
                textCoursesCompleted.setText(String.valueOf(courses));
            }
        });
        
        // Recent activity
        viewModel.getRecentActivity().observe(getViewLifecycleOwner(), activity -> {
            if (recentActivityAdapter != null) {
                recentActivityAdapter.updateData(activity);
            }
        });
        
        // Error message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                if (textError != null) {
                    textError.setText(error);
                }
                if (layoutError != null) {
                    layoutError.setVisibility(View.VISIBLE);
                }
                if (layoutStats != null) {
                    layoutStats.setVisibility(View.GONE);
                }
            } else {
                if (layoutError != null) {
                    layoutError.setVisibility(View.GONE);
                }
                if (layoutStats != null) {
                    layoutStats.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}

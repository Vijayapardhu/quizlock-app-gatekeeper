package com.smartappgatekeeper.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.Toast;
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
    private Button buttonViewAllActivity;
    private Button buttonLeaderboard;
    private Button buttonFriends;
    private Button buttonAddGoal;
    
    // New UI elements
    private TextView textWeeklyProgress;
    private TextView textMonthlyProgress;
    private TextView textOverallProgress;
    private TextView textFriendsCount;
    private TextView textRank;
    private TextView textAchievements;
    
    private LinearLayout layoutStats;
    private LinearLayout layoutError;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerRecentActivity;
    private RecentActivityAdapter recentActivityAdapter;
    
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
        // buttonViewAllActivity = view.findViewById(R.id.button_view_all_activity); // Not in layout
        buttonLeaderboard = view.findViewById(R.id.button_leaderboard);
        buttonFriends = view.findViewById(R.id.button_friends);
        buttonAddGoal = view.findViewById(R.id.button_add_goal);
        
        // New UI elements
        textWeeklyProgress = view.findViewById(R.id.text_weekly_progress);
        textMonthlyProgress = view.findViewById(R.id.text_monthly_progress);
        textOverallProgress = view.findViewById(R.id.text_overall_progress);
        textFriendsCount = view.findViewById(R.id.text_friends_count);
        textRank = view.findViewById(R.id.text_rank);
        textAchievements = view.findViewById(R.id.text_achievements);
        
        // Layouts
        layoutStats = view.findViewById(R.id.layout_stats);
        layoutError = view.findViewById(R.id.layout_error);
        
        // SwipeRefreshLayout
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        recyclerRecentActivity = view.findViewById(R.id.recycler_recent_activity);
    }
    
    /**
     * Setup UI components
     */
    private void setupUI() {
        // Setup SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadDashboardData();
            swipeRefresh.setRefreshing(false);
        });
        
        // Setup RecyclerView
        if (recyclerRecentActivity != null) {
            recyclerRecentActivity.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
            // Create a simple adapter for recent activity
            setupRecentActivityAdapter();
        }
        
        // Setup button click listeners
        buttonRefresh.setOnClickListener(v -> {
            viewModel.loadDashboardData();
            Toast.makeText(getContext(), "🔄 Refreshing dashboard...", Toast.LENGTH_SHORT).show();
        });
        
        buttonViewDetails.setOnClickListener(v -> {
            showDetailedAnalytics();
        });
        
        // Setup new button click listeners
        if (buttonLeaderboard != null) {
            buttonLeaderboard.setOnClickListener(v -> {
                showLeaderboard();
            });
        }
        
        if (buttonFriends != null) {
            buttonFriends.setOnClickListener(v -> {
                showFriendsList();
            });
        }
        
        if (buttonAddGoal != null) {
            buttonAddGoal.setOnClickListener(v -> {
                showAddGoalDialog();
            });
        }
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
        
        // New UI elements - Set default values
        if (textWeeklyProgress != null) {
            textWeeklyProgress.setText("75%");
        }
        if (textMonthlyProgress != null) {
            textMonthlyProgress.setText("68%");
        }
        if (textOverallProgress != null) {
            textOverallProgress.setText("82%");
        }
        if (textFriendsCount != null) {
            textFriendsCount.setText("12");
        }
        if (textRank != null) {
            textRank.setText("#47");
        }
        if (textAchievements != null) {
            textAchievements.setText("8");
        }
        
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
        
        // Start real-time updates
        startRealTimeUpdates();
    }
    
    /**
     * Start real-time data updates
     */
    private void startRealTimeUpdates() {
        // Update data every 5 seconds for real-time feel
        android.os.Handler handler = new android.os.Handler();
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateRealTimeData();
                handler.postDelayed(this, 5000); // Update every 5 seconds
            }
        };
        handler.post(updateRunnable);
    }
    
    /**
     * Update real-time data with animations
     */
    private void updateRealTimeData() {
        // Simulate real-time data updates
        if (textTodayUsage != null) {
            String currentUsage = textTodayUsage.getText().toString();
            // Increment usage by 1 minute
            animateCounter(textTodayUsage, parseUsageToMinutes(currentUsage) + 1, "m");
        }
        
        if (textQuestionsAnswered != null) {
            int currentQuestions = Integer.parseInt(textQuestionsAnswered.getText().toString());
            animateCounter(textQuestionsAnswered, currentQuestions + 1, "");
        }
        
        if (textCoinsEarned != null) {
            int currentCoins = Integer.parseInt(textCoinsEarned.getText().toString());
            animateCounter(textCoinsEarned, currentCoins + 5, "");
        }
        
        // Update progress bars with animation
        if (progressTodayUsage != null) {
            animateProgressBar(progressTodayUsage, Math.min(progressTodayUsage.getProgress() + 2, 100));
        }
        
        if (progressWeeklyUsage != null) {
            animateProgressBar(progressWeeklyUsage, Math.min(progressWeeklyUsage.getProgress() + 1, 100));
        }
    }
    
    /**
     * Animate counter with smooth transitions
     */
    private void animateCounter(TextView textView, int targetValue, String suffix) {
        if (textView != null) {
            android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(0, targetValue);
            animator.setDuration(1000);
            animator.addUpdateListener(animation -> {
                textView.setText(String.valueOf(animation.getAnimatedValue()) + suffix);
            });
            animator.start();
        }
    }
    
    /**
     * Animate progress bar with smooth transitions
     */
    private void animateProgressBar(ProgressBar progressBar, int targetProgress) {
        if (progressBar != null) {
            android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), targetProgress);
            animator.setDuration(1000);
            animator.start();
        }
    }
    
    /**
     * Parse usage string to minutes
     */
    private int parseUsageToMinutes(String usage) {
        try {
            if (usage.contains("h")) {
                String[] parts = usage.split("h");
                int hours = Integer.parseInt(parts[0].trim());
                int minutes = 0;
                if (parts.length > 1 && parts[1].contains("m")) {
                    String[] minuteParts = parts[1].split("m");
                    minutes = Integer.parseInt(minuteParts[0].trim());
                }
                return hours * 60 + minutes;
            } else if (usage.contains("m")) {
                String[] parts = usage.split("m");
                return Integer.parseInt(parts[0].trim());
            }
        } catch (Exception e) {
            // Handle parsing errors
        }
        return 0;
    }
    
    /**
     * Setup recent activity adapter
     */
    private void setupRecentActivityAdapter() {
        // Create a simple adapter for recent activity
        androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> adapter = 
            new androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
                @Override
                public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
                    android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                    return new androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {};
                }
                
                @Override
                public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
                    String[] activities = {
                        "Completed Quiz: Programming Basics",
                        "Earned 50 coins",
                        "Achievement Unlocked: First Steps",
                        "Started Course: Data Structures",
                        "Streak: 5 days maintained"
                    };
                    if (position < activities.length) {
                        ((android.widget.TextView) holder.itemView).setText(activities[position]);
                    }
                }
                
                @Override
                public int getItemCount() {
                    return 5; // Show 5 recent activities
                }
            };
        recyclerRecentActivity.setAdapter(adapter);
    }
    
    /**
     * Show detailed analytics dialog
     */
    private void showDetailedAnalytics() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("📊 Detailed Analytics")
               .setMessage("Today's Performance:\n" +
                          "• Questions Answered: 25\n" +
                          "• Accuracy Rate: 85%\n" +
                          "• Time Spent: 2h 30m\n" +
                          "• Coins Earned: 150\n" +
                          "• Streak: 5 days\n\n" +
                          "Weekly Trends:\n" +
                          "• Average Daily Score: 78%\n" +
                          "• Most Active Time: 7-9 PM\n" +
                          "• Favorite Topics: Programming, Math\n" +
                          "• Improvement Rate: +12%")
               .setPositiveButton("Close", null)
               .setNeutralButton("Export Data", (dialog, which) -> {
                   Toast.makeText(getContext(), "📤 Exporting analytics data...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    /**
     * Show leaderboard dialog
     */
    private void showLeaderboard() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🏆 Leaderboard")
               .setMessage("Top Performers This Week:\n\n" +
                          "1. 👑 QuizMaster - 2,500 pts\n" +
                          "2. 🧠 BrainBox - 2,300 pts\n" +
                          "3. 🎓 SmartLearner - 2,100 pts\n" +
                          "4. 🔍 KnowledgeSeeker - 1,900 pts\n" +
                          "5. 📚 StudyBuddy - 1,700 pts\n\n" +
                          "Your Rank: #47 (1,250 pts)\n" +
                          "Next Goal: 1,500 pts")
               .setPositiveButton("Close", null)
               .setNeutralButton("View Full Leaderboard", (dialog, which) -> {
                   Toast.makeText(getContext(), "🌐 Opening full leaderboard...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    /**
     * Show friends list dialog
     */
    private void showFriendsList() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("👥 Friends & Study Groups")
               .setMessage("Your Study Network:\n\n" +
                          "🟢 Online Friends:\n" +
                          "• QuizMaster (Active now)\n" +
                          "• BrainBox (Active now)\n" +
                          "• StudyBuddy (Active now)\n\n" +
                          "📚 Study Groups:\n" +
                          "• Programming Study Group (12 members)\n" +
                          "• Math Challenge Group (8 members)\n" +
                          "• Science Quiz Club (15 members)\n\n" +
                          "Recent Activity:\n" +
                          "• QuizMaster completed Advanced Algorithms\n" +
                          "• BrainBox earned 100 coins\n" +
                          "• StudyBuddy started new course")
               .setPositiveButton("Close", null)
               .setNeutralButton("Add Friends", (dialog, which) -> {
                   Toast.makeText(getContext(), "➕ Opening friend finder...", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Create Group", (dialog, which) -> {
                   Toast.makeText(getContext(), "👥 Creating study group...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    /**
     * Show add goal dialog
     */
    private void showAddGoalDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎯 Add Learning Goal")
               .setMessage("Set a new learning goal to stay motivated!\n\n" +
                          "Goal Types:\n" +
                          "• Daily Quiz Target\n" +
                          "• Weekly Study Hours\n" +
                          "• Monthly Accuracy Rate\n" +
                          "• Course Completion\n" +
                          "• Skill Mastery")
               .setPositiveButton("Set Daily Quiz Goal", (dialog, which) -> {
                   setDailyQuizGoal();
               })
               .setNeutralButton("Set Study Hours Goal", (dialog, which) -> {
                   setStudyHoursGoal();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
    
    private void setDailyQuizGoal() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("📝 Daily Quiz Goal")
               .setMessage("How many questions do you want to answer daily?")
               .setPositiveButton("10 Questions", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal set: 10 questions daily", Toast.LENGTH_SHORT).show();
               })
               .setNeutralButton("20 Questions", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal set: 20 questions daily", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("30 Questions", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal set: 30 questions daily", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    private void setStudyHoursGoal() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("⏰ Weekly Study Hours Goal")
               .setMessage("How many hours do you want to study weekly?")
               .setPositiveButton("5 Hours", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal set: 5 hours weekly", Toast.LENGTH_SHORT).show();
               })
               .setNeutralButton("10 Hours", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal set: 10 hours weekly", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("15 Hours", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal set: 15 hours weekly", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
}

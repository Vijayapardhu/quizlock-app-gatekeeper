package com.smartappgatekeeper.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Random;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.viewmodel.RoadmapViewModel;
import com.smartappgatekeeper.service.LearningPathService;
import com.smartappgatekeeper.service.QuestionBankService;

import java.util.List;

/**
 * Roadmap Fragment - Learning progress and course roadmaps
 * FR-021: System shall support multiple learning platforms
 * FR-022: System shall display course roadmaps and progress
 * FR-023: System shall track course completion and achievements
 */
public class RoadmapFragment extends Fragment {
    
    private RoadmapViewModel viewModel;
    
    // UI Views
    private TextView textTitle;
    private TextView textProgress;
    private TextView textCurrentStep;
    private TextView textNextStep;
    private ProgressBar progressRoadmap;
    private Button buttonStartCourse;
    private Button buttonViewAchievements;
    
    // New UI Views from layout
    private Button buttonCustomizeRoadmap;
    private Button buttonAddGoal;
    private Button buttonViewAllAchievements;
    private TextView textCoursesInProgress;
    private TextView textCoursesCompleted;
    private TextView textLearningStreak;
    private TextView textOverallProgressPercent;
    private ProgressBar progressOverallRoadmap;
    private ProgressBar progressStep2;
    private ProgressBar progressGoal1;
    private ProgressBar progressGoal2;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadmap, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        initializeViews(view);
        
        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(RoadmapViewModel.class);
        
        // Setup UI
        setupUI();
        
        // Setup interactive elements
        setupInteractiveElements(view);
        
        // Observe ViewModel
        observeViewModel();
        
        // Load data - method doesn't exist yet
        // viewModel.loadRoadmapData();
    }
    
    private void initializeViews(View view) {
        textTitle = view.findViewById(R.id.text_title);
        
        // Initialize all buttons and views from the layout
        buttonCustomizeRoadmap = view.findViewById(R.id.button_customize_roadmap);
        buttonAddGoal = view.findViewById(R.id.button_add_goal);
        buttonViewAllAchievements = view.findViewById(R.id.button_view_all_achievements);
        
        // Initialize text views
        textCoursesInProgress = view.findViewById(R.id.text_courses_in_progress);
        textCoursesCompleted = view.findViewById(R.id.text_courses_completed);
        textLearningStreak = view.findViewById(R.id.text_learning_streak);
        textOverallProgressPercent = view.findViewById(R.id.text_overall_progress_percent);
        
        // Initialize progress bars
        progressOverallRoadmap = view.findViewById(R.id.progress_overall_roadmap);
        progressStep2 = view.findViewById(R.id.progress_step_2);
        progressGoal1 = view.findViewById(R.id.progress_goal_1);
        progressGoal2 = view.findViewById(R.id.progress_goal_2);
    }
    
    private void setupUI() {
        // Setup existing buttons
        if (buttonStartCourse != null) {
            buttonStartCourse.setOnClickListener(v -> {
                showCourseSelection();
            });
        }
        
        if (buttonViewAchievements != null) {
            buttonViewAchievements.setOnClickListener(v -> {
                showAchievements();
            });
        }
        
        // Setup new buttons from layout
        if (buttonCustomizeRoadmap != null) {
            buttonCustomizeRoadmap.setOnClickListener(v -> {
                showCustomizeRoadmap();
            });
        }
        
        if (buttonAddGoal != null) {
            buttonAddGoal.setOnClickListener(v -> {
                showAddGoal();
            });
        }
        
        if (buttonViewAllAchievements != null) {
            buttonViewAllAchievements.setOnClickListener(v -> {
                showAllAchievements();
            });
        }
    }
    
    private void observeViewModel() {
        // Set default values
        if (textTitle != null) {
            textTitle.setText("Learning Roadmap");
        }
        
        // Load comprehensive learning paths
        loadLearningPaths();
        
        // Start real-time progress updates
        startRealtimeProgressUpdates();
    }
    
    /**
     * Setup interactive elements like clickable steps and badges
     */
    private void setupInteractiveElements(View view) {
        // Make learning path steps clickable
        View step1 = view.findViewById(R.id.step_1_container);
        if (step1 != null) {
            step1.setOnClickListener(v -> showStepDetails("Python Basics", "Completed", "100%"));
        }
        
        View step2 = view.findViewById(R.id.step_2_container);
        if (step2 != null) {
            step2.setOnClickListener(v -> showStepDetails("Data Structures & Algorithms", "In Progress", "65%"));
        }
        
        View step3 = view.findViewById(R.id.step_3_container);
        if (step3 != null) {
            step3.setOnClickListener(v -> showStepDetails("Web Development", "Upcoming", "0%"));
        }
        
        // Make achievement badges clickable
        View achievement1 = view.findViewById(R.id.achievement_1);
        if (achievement1 != null) {
            achievement1.setOnClickListener(v -> showAchievementDetails("First Course", "🥇", "Completed your first course!"));
        }
        
        View achievement2 = view.findViewById(R.id.achievement_2);
        if (achievement2 != null) {
            achievement2.setOnClickListener(v -> showAchievementDetails("7-Day Streak", "🔥", "Maintained a 7-day learning streak!"));
        }
        
        View achievement3 = view.findViewById(R.id.achievement_3);
        if (achievement3 != null) {
            achievement3.setOnClickListener(v -> showAchievementDetails("Quiz Master", "🎯", "Answered 100+ quiz questions correctly!"));
        }
        
        // Make goal items clickable
        View goal1 = view.findViewById(R.id.goal_1_container);
        if (goal1 != null) {
            goal1.setOnClickListener(v -> showGoalDetails("Complete 5 courses this month", "3/5", "60%"));
        }
        
        View goal2 = view.findViewById(R.id.goal_2_container);
        if (goal2 != null) {
            goal2.setOnClickListener(v -> showGoalDetails("Maintain 30-day learning streak", "12/30", "40%"));
        }
    }
    
    /**
     * Show step details dialog
     */
    private void showStepDetails(String stepName, String status, String progress) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("📚 " + stepName)
               .setMessage("Status: " + status + "\n" +
                          "Progress: " + progress + "\n\n" +
                          "Description: " + getStepDescription(stepName) + "\n\n" +
                          "Next Steps: " + getNextSteps(stepName))
               .setPositiveButton("Continue Learning", (dialog, which) -> {
                   Toast.makeText(getContext(), "🚀 Continuing with " + stepName, Toast.LENGTH_SHORT).show();
               })
               .setNeutralButton("View Resources", (dialog, which) -> {
                   Toast.makeText(getContext(), "📖 Opening resources for " + stepName, Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Close", null)
               .show();
    }
    
    /**
     * Show achievement details dialog
     */
    private void showAchievementDetails(String achievementName, String emoji, String description) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(emoji + " " + achievementName)
               .setMessage(description + "\n\n" +
                          "Unlocked: " + getAchievementDate(achievementName) + "\n" +
                          "Points: " + getAchievementPoints(achievementName) + "\n\n" +
                          "Keep up the great work!")
               .setPositiveButton("Share", (dialog, which) -> {
                   Toast.makeText(getContext(), "📤 Sharing achievement: " + achievementName, Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Close", null)
               .show();
    }
    
    /**
     * Show goal details dialog
     */
    private void showGoalDetails(String goalName, String progress, String percentage) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎯 Goal Progress")
               .setMessage("Goal: " + goalName + "\n" +
                          "Progress: " + progress + " (" + percentage + ")\n\n" +
                          "Deadline: " + getGoalDeadline(goalName) + "\n" +
                          "Motivation: " + getGoalMotivation(goalName))
               .setPositiveButton("Update Progress", (dialog, which) -> {
                   Toast.makeText(getContext(), "📈 Updating goal progress...", Toast.LENGTH_SHORT).show();
               })
               .setNeutralButton("Edit Goal", (dialog, which) -> {
                   Toast.makeText(getContext(), "✏️ Editing goal...", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Close", null)
               .show();
    }
    
    // Helper methods for step details
    private String getStepDescription(String stepName) {
        switch (stepName) {
            case "Python Basics":
                return "Learn the fundamentals of Python programming including variables, loops, functions, and data structures.";
            case "Data Structures & Algorithms":
                return "Master essential data structures like arrays, linked lists, trees, and common algorithms.";
            case "Web Development":
                return "Build modern web applications using HTML, CSS, JavaScript, and popular frameworks.";
            default:
                return "A comprehensive learning module designed to enhance your skills.";
        }
    }
    
    private String getNextSteps(String stepName) {
        switch (stepName) {
            case "Python Basics":
                return "Move on to Data Structures & Algorithms to build on your Python knowledge.";
            case "Data Structures & Algorithms":
                return "Continue with Web Development to apply your algorithmic thinking in real projects.";
            case "Web Development":
                return "Explore advanced topics like React, Node.js, or mobile development.";
            default:
                return "Continue with the next module in your learning path.";
        }
    }
    
    // Helper methods for achievement details
    private String getAchievementDate(String achievementName) {
        switch (achievementName) {
            case "First Course":
                return "2 weeks ago";
            case "7-Day Streak":
                return "1 week ago";
            case "Quiz Master":
                return "3 days ago";
            default:
                return "Recently";
        }
    }
    
    private String getAchievementPoints(String achievementName) {
        switch (achievementName) {
            case "First Course":
                return "100 points";
            case "7-Day Streak":
                return "200 points";
            case "Quiz Master":
                return "500 points";
            default:
                return "50 points";
        }
    }
    
    // Helper methods for goal details
    private String getGoalDeadline(String goalName) {
        if (goalName.contains("month")) {
            return "December 31, 2024";
        } else if (goalName.contains("30-day")) {
            return "January 15, 2025";
        }
        return "TBD";
    }
    
    private String getGoalMotivation(String goalName) {
        if (goalName.contains("courses")) {
            return "You're doing great! Keep up the momentum to reach your course completion goal.";
        } else if (goalName.contains("streak")) {
            return "Consistency is key! Your daily learning habit is building strong foundations.";
        }
        return "Every step forward is progress toward your learning goals!";
    }
    
    /**
     * Start real-time progress updates
     */
    private void startRealtimeProgressUpdates() {
        android.os.Handler handler = new android.os.Handler();
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateRealtimeProgress();
                handler.postDelayed(this, 8000); // Update every 8 seconds
            }
        };
        handler.post(updateRunnable);
    }
    
    /**
     * Update real-time progress with animations
     */
    private void updateRealtimeProgress() {
        // Simulate progress updates
        Random random = new Random();
        
        // Update progress text with animation
        if (textProgress != null) {
            int currentProgress = extractProgress(textProgress.getText().toString());
            int newProgress = Math.min(currentProgress + random.nextInt(3) + 1, 100);
            animateProgressText(textProgress, newProgress);
        }
        
        // Update current step
        if (textCurrentStep != null) {
            String[] steps = {
                "Introduction to Programming",
                "Data Structures & Algorithms", 
                "Web Development Basics",
                "Mobile App Development",
                "Database Design",
                "Software Testing",
                "Project Management"
            };
            String randomStep = steps[random.nextInt(steps.length)];
            textCurrentStep.setText("Current: " + randomStep);
        }
        
        // Update next step
        if (textNextStep != null) {
            String[] nextSteps = {
                "Advanced Algorithms",
                "Cloud Computing",
                "Machine Learning",
                "DevOps Practices",
                "Cybersecurity",
                "AI & ML Fundamentals"
            };
            String randomNextStep = nextSteps[random.nextInt(nextSteps.length)];
            textNextStep.setText("Next: " + randomNextStep);
        }
        
        // Update progress bar
        if (progressRoadmap != null) {
            int currentProgress = progressRoadmap.getProgress();
            int newProgress = Math.min(currentProgress + random.nextInt(2) + 1, 100);
            animateProgressBar(progressRoadmap, newProgress);
        }
    }
    
    private int extractProgress(String progressText) {
        try {
            if (progressText.contains("%")) {
                String[] parts = progressText.split("%");
                return Integer.parseInt(parts[0].trim());
            }
        } catch (Exception e) {
            // Handle parsing errors
        }
        return 0;
    }
    
    private void animateProgressText(TextView textView, int targetProgress) {
        if (textView != null) {
            android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(0, targetProgress);
            animator.setDuration(1000);
            animator.addUpdateListener(animation -> {
                textView.setText("Progress: " + animation.getAnimatedValue() + "%");
            });
            animator.start();
        }
    }
    
    private void animateProgressBar(ProgressBar progressBar, int targetProgress) {
        if (progressBar != null) {
            android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), targetProgress);
            animator.setDuration(1000);
            animator.start();
        }
    }
    
    /**
     * Show course selection dialog with AI recommendations
     */
    private void showCourseSelection() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎓 Choose Your Learning Path")
               .setMessage("AI-Recommended Courses:\n\n" +
                          "🔥 Hot Picks:\n" +
                          "• Introduction to Programming (Beginner)\n" +
                          "• Data Structures & Algorithms (Intermediate)\n" +
                          "• Web Development with React (Advanced)\n\n" +
                          "📚 By Category:\n" +
                          "• Programming Fundamentals\n" +
                          "• Web Development\n" +
                          "• Mobile Development\n" +
                          "• Data Science\n" +
                          "• Machine Learning")
               .setPositiveButton("Start Programming Course", (dialog, which) -> {
                   startCourse("prog_001", "Introduction to Programming");
               })
               .setNeutralButton("Browse All Courses", (dialog, which) -> {
                   showAllCourses();
               })
               .setNegativeButton("Get AI Recommendation", (dialog, which) -> {
                   getAIRecommendation();
               })
               .show();
    }
    
    private void startCourse(String courseId, String courseName) {
        Toast.makeText(getContext(), "🚀 Starting: " + courseName, Toast.LENGTH_SHORT).show();
        
        // Simulate course start
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎯 Course Started!")
               .setMessage("Welcome to " + courseName + "!\n\n" +
                          "Course Overview:\n" +
                          "• Duration: 20 hours\n" +
                          "• Difficulty: Beginner\n" +
                          "• Prerequisites: None\n" +
                          "• Skills you'll learn: Variables, Loops, Functions\n\n" +
                          "Your progress will be tracked automatically!")
               .setPositiveButton("Start Learning", (dialog, which) -> {
                   Toast.makeText(getContext(), "📚 Opening course content...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    private void showAllCourses() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("📚 All Available Courses")
               .setMessage("Programming:\n" +
                          "• Introduction to Programming (20h)\n" +
                          "• Data Structures & Algorithms (40h)\n" +
                          "• Object-Oriented Programming (25h)\n\n" +
                          "Web Development:\n" +
                          "• HTML & CSS Fundamentals (15h)\n" +
                          "• JavaScript Essentials (30h)\n" +
                          "• React Development (35h)\n\n" +
                          "Mobile Development:\n" +
                          "• Android Development (45h)\n" +
                          "• iOS Development (40h)\n\n" +
                          "Data Science:\n" +
                          "• Python for Data Science (35h)\n" +
                          "• Machine Learning Basics (50h)")
               .setPositiveButton("Close", null)
               .setNeutralButton("Filter by Level", (dialog, which) -> {
                   showCourseFilter();
               })
               .show();
    }
    
    private void showCourseFilter() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🔍 Filter Courses")
               .setMessage("Filter by difficulty level:")
               .setPositiveButton("Beginner", (dialog, which) -> {
                   Toast.makeText(getContext(), "📚 Showing beginner courses...", Toast.LENGTH_SHORT).show();
               })
               .setNeutralButton("Intermediate", (dialog, which) -> {
                   Toast.makeText(getContext(), "📚 Showing intermediate courses...", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Advanced", (dialog, which) -> {
                   Toast.makeText(getContext(), "📚 Showing advanced courses...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    
    /**
     * Show achievements dialog
     */
    private void showAchievements() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🏆 Your Achievements")
               .setMessage("Recent Achievements:\n\n" +
                          "🥇 Gold Achievements:\n" +
                          "• Quiz Master (100+ questions)\n" +
                          "• Streak Champion (7 days)\n" +
                          "• Speed Demon (10 questions/min)\n\n" +
                          "🥈 Silver Achievements:\n" +
                          "• First Steps (10 questions)\n" +
                          "• Consistency (3 day streak)\n" +
                          "• Accuracy Expert (80%+)\n\n" +
                          "🥉 Bronze Achievements:\n" +
                          "• Getting Started (1 question)\n" +
                          "• First Coin (10 coins)\n\n" +
                          "Progress: 8/15 achievements unlocked\n" +
                          "Next Goal: Complete 5 courses")
               .setPositiveButton("Close", null)
               .setNeutralButton("View All", (dialog, which) -> {
                   showAllAchievements();
               })
               .setNegativeButton("Share", (dialog, which) -> {
                   Toast.makeText(getContext(), "📤 Sharing achievements...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    private void showAllAchievements() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🏆 All Achievements")
               .setMessage("Unlocked (8/15):\n" +
                          "✅ Quiz Master\n" +
                          "✅ Streak Champion\n" +
                          "✅ Speed Demon\n" +
                          "✅ First Steps\n" +
                          "✅ Consistency\n" +
                          "✅ Accuracy Expert\n" +
                          "✅ Getting Started\n" +
                          "✅ First Coin\n\n" +
                          "Locked (7/15):\n" +
                          "🔒 Marathon Runner (500+ questions)\n" +
                          "🔒 Perfectionist (95%+ accuracy)\n" +
                          "🔒 Course Master (5 courses)\n" +
                          "🔒 Social Butterfly (10 friends)\n" +
                          "🔒 Night Owl (late night study)\n" +
                          "🔒 Early Bird (morning study)\n" +
                          "🔒 Legend (1000+ questions)")
               .setPositiveButton("Close", null)
               .setNeutralButton("Set Goals", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Setting achievement goals...", Toast.LENGTH_SHORT).show();
               })
               .show();
    }
    
    /**
     * Load comprehensive learning paths
     */
    private void loadLearningPaths() {
        LearningPathService pathService = LearningPathService.getInstance();
        List<LearningPathService.LearningPath> paths = pathService.getAllLearningPaths();
        
        // Update UI with learning paths
        updateLearningPathsUI(paths);
    }
    
    /**
     * Update UI with learning paths
     */
    private void updateLearningPathsUI(List<LearningPathService.LearningPath> paths) {
        if (paths == null || paths.isEmpty()) {
            return;
        }
        
        // Create a summary of available learning paths
        StringBuilder pathSummary = new StringBuilder("Available Learning Paths:\n\n");
        
        for (LearningPathService.LearningPath path : paths) {
            pathSummary.append("📚 ").append(path.title)
                      .append("\n   Difficulty: ").append(path.difficulty)
                      .append("\n   Duration: ").append(path.estimatedHours).append(" hours")
                      .append("\n   Topics: ").append(String.join(", ", path.topics))
                      .append("\n\n");
        }
        
        // Update the progress text with learning paths
        if (textProgress != null) {
            textProgress.setText(pathSummary.toString());
        }
    }
    
    /**
     * Get AI recommendation for learning path
     */
    private void getAIRecommendation() {
        LearningPathService pathService = LearningPathService.getInstance();
        
        // Get user's quiz performance to recommend paths
        String[] completedPaths = {}; // TODO: Get from user profile
        String userLevel = "Beginner"; // TODO: Determine from performance
        
        List<LearningPathService.LearningPath> recommendations = 
            pathService.getRecommendedPaths(userLevel, completedPaths);
        
        if (!recommendations.isEmpty()) {
            LearningPathService.LearningPath recommended = recommendations.get(0);
            
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("🤖 AI Recommendation")
                   .setMessage("Based on your progress, I recommend:\n\n" +
                             "📚 " + recommended.title + "\n" +
                             "📖 " + recommended.description + "\n" +
                             "⏱️ " + recommended.estimatedHours + " hours\n" +
                             "🎯 Topics: " + String.join(", ", recommended.topics))
                   .setPositiveButton("Start Learning", (dialog, which) -> {
                       startLearningPath(recommended.id);
                   })
                   .setNegativeButton("View All Paths", (dialog, which) -> {
                       showAllLearningPaths();
                   })
                   .show();
        } else {
            android.widget.Toast.makeText(getContext(), 
                "No recommendations available at the moment", 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Start a specific learning path
     */
    private void startLearningPath(String pathId) {
        LearningPathService pathService = LearningPathService.getInstance();
        LearningPathService.LearningPath path = pathService.getLearningPathById(pathId);
        
        if (path != null) {
            android.widget.Toast.makeText(getContext(), 
                "Starting: " + path.title, 
                android.widget.Toast.LENGTH_LONG).show();
            
            // TODO: Navigate to learning path details or start first lesson
        }
    }
    
    /**
     * Show all available learning paths
     */
    private void showAllLearningPaths() {
        LearningPathService pathService = LearningPathService.getInstance();
        List<LearningPathService.LearningPath> allPaths = pathService.getAllLearningPaths();
        
        String[] pathTitles = new String[allPaths.size()];
        for (int i = 0; i < allPaths.size(); i++) {
            pathTitles[i] = allPaths.get(i).title + " (" + allPaths.get(i).difficulty + ")";
        }
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("📚 All Learning Paths")
               .setItems(pathTitles, (dialog, which) -> {
                   LearningPathService.LearningPath selectedPath = allPaths.get(which);
                   showLearningPathDetails(selectedPath);
               })
               .show();
    }
    
    /**
     * Show detailed information about a learning path
     */
    private void showLearningPathDetails(LearningPathService.LearningPath path) {
        StringBuilder details = new StringBuilder();
        details.append("📚 ").append(path.title).append("\n\n");
        details.append("📖 ").append(path.description).append("\n\n");
        details.append("🎯 Difficulty: ").append(path.difficulty).append("\n");
        details.append("⏱️ Duration: ").append(path.estimatedHours).append(" hours\n\n");
        details.append("📋 Topics Covered:\n");
        for (String topic : path.topics) {
            details.append("• ").append(topic).append("\n");
        }
        details.append("\n🎯 Learning Objectives:\n");
        for (String objective : path.learningObjectives) {
            details.append("• ").append(objective).append("\n");
        }
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Learning Path Details")
               .setMessage(details.toString())
               .setPositiveButton("Start Learning", (dialog, which) -> {
                   startLearningPath(path.id);
               })
               .setNegativeButton("Close", null)
               .show();
    }
    
    /**
     * Show customize roadmap dialog
     */
    private void showCustomizeRoadmap() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎨 Customize Your Roadmap")
               .setMessage("Personalize your learning journey:\n\n" +
                          "📚 Learning Style:\n" +
                          "• Visual Learner\n" +
                          "• Hands-on Practice\n" +
                          "• Theory Focus\n\n" +
                          "⏰ Time Commitment:\n" +
                          "• 30 minutes/day\n" +
                          "• 1 hour/day\n" +
                          "• 2+ hours/day\n\n" +
                          "🎯 Focus Areas:\n" +
                          "• Programming\n" +
                          "• Web Development\n" +
                          "• Data Science\n" +
                          "• Mobile Development")
               .setPositiveButton("Save Preferences", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎨 Roadmap customized successfully!", Toast.LENGTH_SHORT).show();
               })
               .setNeutralButton("Reset to Default", (dialog, which) -> {
                   Toast.makeText(getContext(), "🔄 Roadmap reset to default settings", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
    
    /**
     * Show add goal dialog
     */
    private void showAddGoal() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎯 Add Learning Goal")
               .setMessage("Set a new learning goal:\n\n" +
                          "📚 Goal Types:\n" +
                          "• Complete X courses\n" +
                          "• Study X hours per week\n" +
                          "• Maintain X-day streak\n" +
                          "• Master specific skill\n\n" +
                          "⏰ Timeframes:\n" +
                          "• This week\n" +
                          "• This month\n" +
                          "• Next 3 months\n" +
                          "• This year")
               .setPositiveButton("Create Goal", (dialog, which) -> {
                   showCreateGoalForm();
               })
               .setNeutralButton("Quick Goals", (dialog, which) -> {
                   showQuickGoals();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
    
    /**
     * Show create goal form
     */
    private void showCreateGoalForm() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("📝 Create New Goal")
               .setMessage("Goal: Complete 3 courses this month\n" +
                          "Deadline: December 31, 2024\n" +
                          "Progress: 0/3 courses\n\n" +
                          "This goal will help you stay motivated and track your learning progress!")
               .setPositiveButton("Save Goal", (dialog, which) -> {
                   Toast.makeText(getContext(), "🎯 Goal created successfully!", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
    
    /**
     * Show quick goals
     */
    private void showQuickGoals() {
        String[] quickGoals = {
            "Study 1 hour today",
            "Complete 5 quiz questions",
            "Review yesterday's lesson",
            "Practice coding for 30 minutes",
            "Read one chapter",
            "Watch 2 tutorial videos"
        };
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("⚡ Quick Goals")
               .setItems(quickGoals, (dialog, which) -> {
                   String selectedGoal = quickGoals[which];
                   Toast.makeText(getContext(), "🎯 Added: " + selectedGoal, Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
}

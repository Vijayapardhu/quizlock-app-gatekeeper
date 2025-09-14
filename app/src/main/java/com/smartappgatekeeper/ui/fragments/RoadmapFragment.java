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
        
        // Observe ViewModel
        observeViewModel();
        
        // Load data - method doesn't exist yet
        // viewModel.loadRoadmapData();
    }
    
    private void initializeViews(View view) {
        textTitle = view.findViewById(R.id.text_title);
        // Only initialize views that exist in the layout
        // Other views will be null and handled gracefully
    }
    
    private void setupUI() {
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
}

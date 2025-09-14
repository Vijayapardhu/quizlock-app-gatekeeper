package com.smartappgatekeeper.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.adapters.AchievementsAdapter;
import com.smartappgatekeeper.model.Achievement;

import java.util.ArrayList;
import java.util.List;

/**
 * Achievements Activity - Apple-style achievements display
 * Shows user achievements and progress
 */
public class AchievementsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewAchievements;
    private LinearLayout layoutStats;
    private TextView textTotalAchievements, textCompletedAchievements, textProgress;
    
    private AchievementsAdapter achievementsAdapter;
    private List<Achievement> achievementsList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        
        initViews();
        setupRecyclerView();
        loadAchievements();
        updateStats();
    }
    
    private void initViews() {
        recyclerViewAchievements = findViewById(R.id.recycler_view_achievements);
        layoutStats = findViewById(R.id.layout_stats);
        textTotalAchievements = findViewById(R.id.text_total_achievements);
        textCompletedAchievements = findViewById(R.id.text_completed_achievements);
        textProgress = findViewById(R.id.text_progress);
    }
    
    private void setupRecyclerView() {
        achievementsAdapter = new AchievementsAdapter(achievementsList);
        recyclerViewAchievements.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAchievements.setAdapter(achievementsAdapter);
    }
    
    private void loadAchievements() {
        achievementsList.clear();
        
        // Quiz Achievements
        achievementsList.add(new Achievement(
            "quiz_master", "Quiz Master", "Complete 25 quizzes", 
            "Complete 25 quizzes to unlock this achievement", 
            R.drawable.ic_quiz, 25, 18, true, false
        ));
        
        achievementsList.add(new Achievement(
            "perfect_score", "Perfect Score", "Get 100% on a quiz", 
            "Answer all questions correctly in a single quiz", 
            R.drawable.ic_quiz, 1, 0, false, false
        ));
        
        achievementsList.add(new Achievement(
            "streak_7", "7-Day Streak", "Quiz for 7 days in a row", 
            "Complete at least one quiz every day for 7 days", 
            R.drawable.ic_timer, 7, 3, false, false
        ));
        
        achievementsList.add(new Achievement(
            "streak_30", "30-Day Streak", "Quiz for 30 days in a row", 
            "Complete at least one quiz every day for 30 days", 
            R.drawable.ic_timer, 30, 3, false, false
        ));
        
        // Learning Achievements
        achievementsList.add(new Achievement(
            "course_complete", "Course Graduate", "Complete 5 courses", 
            "Finish 5 complete learning courses", 
            R.drawable.ic_roadmap, 5, 2, false, false
        ));
        
        achievementsList.add(new Achievement(
            "ai_guide", "AI Assistant", "Use AI guidance 10 times", 
            "Get help from AI guidance 10 times", 
            R.drawable.ic_ai_bot, 10, 7, false, false
        ));
        
        // Social Achievements
        achievementsList.add(new Achievement(
            "friend_maker", "Social Butterfly", "Add 5 friends", 
            "Connect with 5 friends on the platform", 
            R.drawable.ic_profile, 5, 1, false, false
        ));
        
        achievementsList.add(new Achievement(
            "leaderboard", "Top Performer", "Reach top 10 on leaderboard", 
            "Make it to the top 10 on the weekly leaderboard", 
            R.drawable.ic_reports, 1, 0, false, false
        ));
        
        achievementsAdapter.notifyDataSetChanged();
    }
    
    private void updateStats() {
        int total = achievementsList.size();
        int completed = 0;
        
        for (Achievement achievement : achievementsList) {
            if (achievement.isCompleted()) {
                completed++;
            }
        }
        
        textTotalAchievements.setText(String.valueOf(total));
        textCompletedAchievements.setText(String.valueOf(completed));
        textProgress.setText(String.format("%.0f%%", (completed * 100.0 / total)));
    }
}

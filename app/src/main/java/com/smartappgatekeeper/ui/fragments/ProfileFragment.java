package com.smartappgatekeeper.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.activities.EditProfileActivity;
import com.smartappgatekeeper.ui.activities.AddFriendsActivity;
import com.smartappgatekeeper.ui.activities.AchievementsActivity;
import com.smartappgatekeeper.ui.activities.DataExportActivity;

/**
 * Profile Fragment - Apple-style user profile management
 * Replaces SocialFragment with user profile, friends, and settings
 */
public class ProfileFragment extends Fragment {
    
    private TextView textUserName, textUserEmail, textUserLevel, textUserStats;
    private ImageView imageUserAvatar;
    private LinearLayout layoutFriends, layoutAchievements;
    private Button buttonEditProfile, buttonAddFriends, buttonViewAchievements, buttonExportData;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setupClickListeners();
        loadProfileData();
        return view;
    }
    
    private void initViews(View view) {
        textUserName = view.findViewById(R.id.text_user_name);
        textUserEmail = view.findViewById(R.id.text_user_email);
        textUserLevel = view.findViewById(R.id.text_user_level);
        textUserStats = view.findViewById(R.id.text_user_stats);
        
        imageUserAvatar = view.findViewById(R.id.image_user_avatar);
        
        layoutFriends = view.findViewById(R.id.layout_friends);
        layoutAchievements = view.findViewById(R.id.layout_achievements);
        
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        buttonAddFriends = view.findViewById(R.id.button_add_friends);
        buttonViewAchievements = view.findViewById(R.id.button_view_achievements);
        buttonExportData = view.findViewById(R.id.button_export_data);
    }
    
    private void setupClickListeners() {
        buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });
        
        buttonAddFriends.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddFriendsActivity.class);
            startActivity(intent);
        });
        
        buttonViewAchievements.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AchievementsActivity.class);
            startActivity(intent);
        });
        
        buttonExportData.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DataExportActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadProfileData() {
        // Load user profile data
        textUserName.setText("John Doe");
        textUserEmail.setText("john.doe@example.com");
        textUserLevel.setText("Level 5 - Quiz Master");
        
        // Load user stats
        textUserStats.setText("Quizzes Completed: 25\n" +
                            "Average Score: 85%\n" +
                            "Current Streak: 7 days\n" +
                            "Total Coins: 1,250");
        
        // Set default avatar
        imageUserAvatar.setImageResource(R.drawable.ic_profile);
        
        // Load friends list (placeholder)
        loadFriendsList();
        
        // Load achievements (placeholder)
        loadAchievements();
    }
    
    private void loadFriendsList() {
        // TODO: Implement friends list loading
        // This would load from database or API
    }
    
    private void loadAchievements() {
        // TODO: Implement achievements loading
        // This would load from database or API
    }
}

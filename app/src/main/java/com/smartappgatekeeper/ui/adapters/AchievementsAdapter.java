package com.smartappgatekeeper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.model.Achievement;

import java.util.List;

/**
 * Achievements Adapter for RecyclerView
 * Displays achievements list with progress
 */
public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder> {
    
    private List<Achievement> achievementsList;
    
    public AchievementsAdapter(List<Achievement> achievementsList) {
        this.achievementsList = achievementsList;
    }
    
    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievementsList.get(position);
        holder.bind(achievement);
    }
    
    @Override
    public int getItemCount() {
        return achievementsList.size();
    }
    
    class AchievementViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageIcon, imageStatus;
        private TextView textTitle, textDescription, textProgress;
        private ProgressBar progressBar;
        
        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_achievement_icon);
            imageStatus = itemView.findViewById(R.id.image_achievement_status);
            textTitle = itemView.findViewById(R.id.text_achievement_title);
            textDescription = itemView.findViewById(R.id.text_achievement_description);
            textProgress = itemView.findViewById(R.id.text_achievement_progress);
            progressBar = itemView.findViewById(R.id.progress_achievement);
        }
        
        public void bind(Achievement achievement) {
            textTitle.setText(achievement.getTitle());
            textDescription.setText(achievement.getDescription());
            
            // Set icon
            imageIcon.setImageResource(achievement.getIconResId());
            
            // Set status
            if (achievement.isCompleted()) {
                imageStatus.setImageResource(R.drawable.ic_quiz); // Use checkmark icon
                imageStatus.setVisibility(View.VISIBLE);
                textProgress.setText("Completed!");
                textProgress.setTextColor(itemView.getContext().getColor(R.color.success));
            } else if (achievement.isLocked()) {
                imageStatus.setImageResource(R.drawable.ic_settings); // Use lock icon
                imageStatus.setVisibility(View.VISIBLE);
                textProgress.setText("Locked");
                textProgress.setTextColor(itemView.getContext().getColor(R.color.text_tertiary));
            } else {
                imageStatus.setVisibility(View.GONE);
                int progress = achievement.getProgressPercentage();
                textProgress.setText(String.format("%d/%d (%d%%)", 
                    achievement.getCurrentValue(), achievement.getTargetValue(), progress));
                textProgress.setTextColor(itemView.getContext().getColor(R.color.primary_color));
            }
            
            // Set progress bar
            if (achievement.isCompleted()) {
                progressBar.setProgress(100);
            } else if (achievement.isLocked()) {
                progressBar.setProgress(0);
            } else {
                progressBar.setProgress(achievement.getProgressPercentage());
            }
            
            // Set alpha for locked achievements
            float alpha = achievement.isLocked() ? 0.5f : 1.0f;
            itemView.setAlpha(alpha);
        }
    }
}

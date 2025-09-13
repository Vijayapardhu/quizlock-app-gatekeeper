package com.smartappgatekeeper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.database.entities.UsageEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for recent activity RecyclerView
 * Displays recent usage events in dashboard
 */
public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ViewHolder> {
    
    private List<UsageEvent> activities = new ArrayList<>();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_recent_activity, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsageEvent activity = activities.get(position);
        holder.bind(activity);
    }
    
    @Override
    public int getItemCount() {
        return activities.size();
    }
    
    /**
     * Update the data list
     */
    public void updateData(List<UsageEvent> newActivities) {
        activities.clear();
        if (newActivities != null) {
            activities.addAll(newActivities);
        }
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder for activity items
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textAppName;
        private TextView textEventType;
        private TextView textTimestamp;
        private TextView textDuration;
        private TextView textSuccess;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAppName = itemView.findViewById(R.id.text_app_name);
            textEventType = itemView.findViewById(R.id.text_event_type);
            textTimestamp = itemView.findViewById(R.id.text_timestamp);
            textDuration = itemView.findViewById(R.id.text_duration);
            textSuccess = itemView.findViewById(R.id.text_success);
        }
        
        public void bind(UsageEvent activity) {
            textAppName.setText(activity.appName != null ? activity.appName : "Unknown App");
            textEventType.setText(formatEventType(activity.eventType));
            textTimestamp.setText(formatTimestamp(activity.timestamp));
            textDuration.setText(formatDuration(activity.durationSeconds));
            textSuccess.setText(activity.success ? "✓" : "✗");
            textSuccess.setTextColor(activity.success ? 
                itemView.getContext().getColor(R.color.success) : 
                itemView.getContext().getColor(R.color.error));
        }
        
        private String formatEventType(String eventType) {
            if (eventType == null) return "Unknown";
            
            switch (eventType) {
                case "app_launch": return "App Launch";
                case "quiz_attempt": return "Quiz Attempt";
                case "quiz_success": return "Quiz Success";
                case "quiz_failure": return "Quiz Failure";
                case "app_unlock": return "App Unlocked";
                case "app_lock": return "App Locked";
                default: return eventType;
            }
        }
        
        private String formatTimestamp(Date timestamp) {
            if (timestamp == null) return "Unknown";
            
            Date now = new Date();
            long diff = now.getTime() - timestamp.getTime();
            long minutes = diff / (1000 * 60);
            
            if (minutes < 1) {
                return "Just now";
            } else if (minutes < 60) {
                return minutes + "m ago";
            } else if (minutes < 1440) { // 24 hours
                return (minutes / 60) + "h ago";
            } else {
                return new SimpleDateFormat("MMM dd", Locale.getDefault()).format(timestamp);
            }
        }
        
        private String formatDuration(long durationSeconds) {
            if (durationSeconds <= 0) return "";
            
            if (durationSeconds < 60) {
                return durationSeconds + "s";
            } else {
                long minutes = durationSeconds / 60;
                long seconds = durationSeconds % 60;
                return minutes + "m " + seconds + "s";
            }
        }
    }
}

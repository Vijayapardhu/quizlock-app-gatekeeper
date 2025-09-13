package com.quizlock.gatekeeper.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizlock.gatekeeper.R;
import com.quizlock.gatekeeper.data.model.AppUsage;
import com.quizlock.gatekeeper.utils.AppInfoHelper;

import java.util.List;

/**
 * Adapter for displaying blocked apps in RecyclerView
 */
public class BlockedAppsAdapter extends RecyclerView.Adapter<BlockedAppsAdapter.ViewHolder> {
    
    private List<AppUsage> blockedApps;
    private Context context;
    
    public BlockedAppsAdapter(List<AppUsage> blockedApps) {
        this.blockedApps = blockedApps;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_blocked_app, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppUsage appUsage = blockedApps.get(position);
        holder.bind(appUsage);
    }
    
    @Override
    public int getItemCount() {
        return blockedApps.size();
    }
    
    public void updateData(List<AppUsage> newData) {
        this.blockedApps = newData;
        notifyDataSetChanged();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAppIcon;
        private TextView tvAppName;
        private TextView tvLastUsed;
        private TextView tvBlockedAttempts;
        private View viewStatusIndicator;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAppIcon = itemView.findViewById(R.id.iv_app_icon);
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            tvLastUsed = itemView.findViewById(R.id.tv_last_used);
            tvBlockedAttempts = itemView.findViewById(R.id.tv_blocked_attempts);
            viewStatusIndicator = itemView.findViewById(R.id.view_status_indicator);
        }
        
        public void bind(AppUsage appUsage) {
            // Set app name
            String appName = appUsage.getAppName();
            if (appName == null || appName.isEmpty()) {
                appName = AppInfoHelper.getAppName(context, appUsage.getPackageName());
            }
            tvAppName.setText(appName);
            
            // Set app icon
            try {
                ivAppIcon.setImageDrawable(AppInfoHelper.getAppIcon(context, appUsage.getPackageName()));
            } catch (Exception e) {
                ivAppIcon.setImageResource(R.mipmap.ic_launcher);
            }
            
            // Set last used time
            String lastUsedText = AppInfoHelper.getTimeAgoText(appUsage.getLastUsed());
            tvLastUsed.setText("Last used " + lastUsedText);
            
            // Set blocked attempts
            int attempts = appUsage.getBlockedAttempts();
            if (attempts > 0) {
                tvBlockedAttempts.setText(attempts + " blocks");
                tvBlockedAttempts.setVisibility(View.VISIBLE);
            } else {
                tvBlockedAttempts.setVisibility(View.GONE);
            }
            
            // Set status indicator based on current unlock status
            if (appUsage.isCurrentlyUnlocked()) {
                viewStatusIndicator.setBackgroundTintList(
                    androidx.core.content.ContextCompat.getColorStateList(context, R.color.success)
                );
            } else {
                viewStatusIndicator.setBackgroundTintList(
                    androidx.core.content.ContextCompat.getColorStateList(context, R.color.error)
                );
            }
        }
    }
}
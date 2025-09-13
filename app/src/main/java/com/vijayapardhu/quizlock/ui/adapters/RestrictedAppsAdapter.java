package com.vijayapardhu.quizlock.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vijayapardhu.quizlock.databinding.ItemRestrictedAppBinding;
import com.vijayapardhu.quizlock.db.RestrictedApp;
import com.vijayapardhu.quizlock.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class RestrictedAppsAdapter extends RecyclerView.Adapter<RestrictedAppsAdapter.AppViewHolder> {
    
    private List<RestrictedApp> apps = new ArrayList<>();
    
    public void updateApps(List<RestrictedApp> newApps) {
        this.apps = newApps;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestrictedAppBinding binding = ItemRestrictedAppBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AppViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        holder.bind(apps.get(position));
    }
    
    @Override
    public int getItemCount() {
        return apps.size();
    }
    
    static class AppViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestrictedAppBinding binding;
        
        public AppViewHolder(ItemRestrictedAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(RestrictedApp app) {
            binding.tvAppName.setText(app.getAppName());
            binding.tvDailyLimit.setText("Daily limit: " + AppUtils.formatDuration(app.getDailyLimitMinutes()));
            binding.switchRestricted.setChecked(app.isRestricted());
            
            // Load app icon
            try {
                binding.ivAppIcon.setImageDrawable(
                    AppUtils.getAppIcon(binding.getRoot().getContext(), app.getPackageName())
                );
            } catch (Exception e) {
                // Use default icon if app icon not found
                binding.ivAppIcon.setImageResource(android.R.drawable.sym_def_app_icon);
            }
            
            // Handle switch toggle
            binding.switchRestricted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // TODO: Update restriction status in database
            });
        }
    }
}
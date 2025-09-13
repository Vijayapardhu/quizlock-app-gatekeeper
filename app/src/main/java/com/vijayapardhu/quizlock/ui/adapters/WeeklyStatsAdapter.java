package com.vijayapardhu.quizlock.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vijayapardhu.quizlock.databinding.ItemDailyStatBinding;
import com.vijayapardhu.quizlock.viewmodel.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class WeeklyStatsAdapter extends RecyclerView.Adapter<WeeklyStatsAdapter.StatViewHolder> {
    
    private List<DashboardViewModel.DailyUsageStats> stats = new ArrayList<>();
    
    public void updateStats(List<DashboardViewModel.DailyUsageStats> newStats) {
        this.stats = newStats;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDailyStatBinding binding = ItemDailyStatBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new StatViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        holder.bind(stats.get(position));
    }
    
    @Override
    public int getItemCount() {
        return stats.size();
    }
    
    static class StatViewHolder extends RecyclerView.ViewHolder {
        private final ItemDailyStatBinding binding;
        
        public StatViewHolder(ItemDailyStatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(DashboardViewModel.DailyUsageStats stat) {
            binding.tvDay.setText(stat.day);
            binding.tvQuizCount.setText(String.valueOf(stat.quizCount));
            
            // Set usage bar height based on usage (max height represents max usage)
            int maxHeight = 60; // dp
            int maxUsage = 240; // 4 hours in minutes
            int barHeight = Math.min(maxHeight, (stat.usageMinutes * maxHeight) / maxUsage);
            
            ViewGroup.LayoutParams params = binding.viewUsageBar.getLayoutParams();
            params.height = barHeight * (int) binding.getRoot().getContext().getResources().getDisplayMetrics().density;
            binding.viewUsageBar.setLayoutParams(params);
        }
    }
}
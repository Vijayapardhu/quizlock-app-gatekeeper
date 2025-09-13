package com.vijayapardhu.quizlock.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vijayapardhu.quizlock.databinding.FragmentDashboardBinding;
import com.vijayapardhu.quizlock.ui.adapters.WeeklyStatsAdapter;
import com.vijayapardhu.quizlock.utils.AppUtils;
import com.vijayapardhu.quizlock.viewmodel.DashboardViewModel;

public class DashboardFragment extends Fragment {
    
    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private WeeklyStatsAdapter weeklyStatsAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        
        setupRecyclerView();
        observeViewModel();
    }
    
    private void setupRecyclerView() {
        weeklyStatsAdapter = new WeeklyStatsAdapter();
        binding.recyclerWeeklyStats.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.recyclerWeeklyStats.setAdapter(weeklyStatsAdapter);
    }
    
    private void observeViewModel() {
        viewModel.getCurrentStreak().observe(getViewLifecycleOwner(), streak -> {
            binding.tvCurrentStreak.setText(streak);
        });
        
        viewModel.getBestStreak().observe(getViewLifecycleOwner(), bestStreak -> {
            binding.tvBestStreak.setText(bestStreak + " days");
        });
        
        viewModel.getTodayCorrectAnswers().observe(getViewLifecycleOwner(), correctAnswers -> {
            binding.tvTodayQuestions.setText(String.valueOf(correctAnswers != null ? correctAnswers : 0));
        });
        
        viewModel.getTodayTotalUsage().observe(getViewLifecycleOwner(), totalUsage -> {
            String usageText = AppUtils.formatDuration(totalUsage != null ? totalUsage : 0);
            binding.tvTodayUsage.setText(usageText);
        });
        
        viewModel.getCorrectQuizzesToday().observe(getViewLifecycleOwner(), correctQuizzes -> {
            binding.tvCorrectQuizzes.setText("✓ " + (correctQuizzes != null ? correctQuizzes : 0) + " quizzes");
        });
        
        viewModel.getTotalCorrectAnswers().observe(getViewLifecycleOwner(), totalQuestions -> {
            binding.tvTotalQuestions.setText(String.valueOf(totalQuestions != null ? totalQuestions : 0));
        });
        
        viewModel.getWeeklyStats().observe(getViewLifecycleOwner(), weeklyStats -> {
            if (weeklyStats != null) {
                weeklyStatsAdapter.updateStats(weeklyStats);
            }
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
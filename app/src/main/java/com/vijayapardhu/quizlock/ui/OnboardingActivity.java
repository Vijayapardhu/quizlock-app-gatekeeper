package com.vijayapardhu.quizlock.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vijayapardhu.quizlock.R;
import com.vijayapardhu.quizlock.databinding.ActivityOnboardingBinding;
import com.vijayapardhu.quizlock.ui.fragments.onboarding.OnboardingAppsFragment;
import com.vijayapardhu.quizlock.ui.fragments.onboarding.OnboardingTopicsFragment;
import com.vijayapardhu.quizlock.ui.fragments.onboarding.OnboardingWelcomeFragment;
import com.vijayapardhu.quizlock.viewmodel.OnboardingViewModel;

public class OnboardingActivity extends AppCompatActivity {
    
    private ActivityOnboardingBinding binding;
    private OnboardingViewModel viewModel;
    private int currentPage = 0;
    private final int totalPages = 3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);
        
        setupViewPager();
        setupButtons();
        observeViewModel();
    }
    
    private void setupViewPager() {
        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter();
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false); // Disable swiping
    }
    
    private void setupButtons() {
        binding.btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                binding.viewPager.setCurrentItem(currentPage, true);
                updateButtons();
            } else {
                // Complete onboarding
                viewModel.completeOnboarding();
            }
        });
        
        binding.btnBack.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                binding.viewPager.setCurrentItem(currentPage, true);
                updateButtons();
            }
        });
    }
    
    private void updateButtons() {
        binding.btnBack.setVisibility(currentPage > 0 ? android.view.View.VISIBLE : android.view.View.GONE);
        
        if (currentPage == totalPages - 1) {
            binding.btnNext.setText(R.string.finish);
        } else {
            binding.btnNext.setText(R.string.next);
        }
    }
    
    private void observeViewModel() {
        viewModel.getOnboardingComplete().observe(this, isComplete -> {
            if (isComplete != null && isComplete) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                android.widget.Toast.makeText(this, error, android.widget.Toast.LENGTH_LONG).show();
            }
        });
        
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnNext.setEnabled(!isLoading);
            binding.btnBack.setEnabled(!isLoading);
        });
    }
    
    private class OnboardingPagerAdapter extends FragmentStateAdapter {
        
        public OnboardingPagerAdapter() {
            super(OnboardingActivity.this);
        }
        
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new OnboardingWelcomeFragment();
                case 1:
                    return new OnboardingAppsFragment();
                case 2:
                    return new OnboardingTopicsFragment();
                default:
                    return new OnboardingWelcomeFragment();
            }
        }
        
        @Override
        public int getItemCount() {
            return totalPages;
        }
    }
}
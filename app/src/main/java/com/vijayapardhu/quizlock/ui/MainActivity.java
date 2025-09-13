package com.vijayapardhu.quizlock.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vijayapardhu.quizlock.R;
import com.vijayapardhu.quizlock.databinding.ActivityMainBinding;
import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.UserPreferencesDao;
import com.vijayapardhu.quizlock.ui.fragments.AppsFragment;
import com.vijayapardhu.quizlock.ui.fragments.DashboardFragment;
import com.vijayapardhu.quizlock.ui.fragments.SettingsFragment;
import com.vijayapardhu.quizlock.utils.Constants;
import com.vijayapardhu.quizlock.utils.NotificationUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    private UserPreferencesDao userPreferencesDao;
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Initialize database
        QuizLockDatabase database = QuizLockDatabase.getDatabase(this);
        userPreferencesDao = database.userPreferencesDao();
        executor = Executors.newSingleThreadExecutor();
        
        // Create notification channels
        NotificationUtils.createNotificationChannels(this);
        
        // Check if onboarding is completed
        checkOnboardingStatus();
        
        // Setup UI
        setupViewPager();
        setupBottomNavigation();
    }
    
    private void checkOnboardingStatus() {
        executor.execute(() -> {
            String onboardingCompleted = userPreferencesDao.getPreference(Constants.PREF_ONBOARDING_COMPLETED);
            if (onboardingCompleted == null || !onboardingCompleted.equals("true")) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(this, OnboardingActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }
    
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false); // Disable swiping
    }
    
    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                binding.viewPager.setCurrentItem(0, false);
                return true;
            } else if (itemId == R.id.nav_apps) {
                binding.viewPager.setCurrentItem(1, false);
                return true;
            } else if (itemId == R.id.nav_settings) {
                binding.viewPager.setCurrentItem(2, false);
                return true;
            }
            return false;
        });
    }
    
    private class ViewPagerAdapter extends FragmentStateAdapter {
        
        public ViewPagerAdapter() {
            super(MainActivity.this);
        }
        
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new DashboardFragment();
                case 1:
                    return new AppsFragment();
                case 2:
                    return new SettingsFragment();
                default:
                    return new DashboardFragment();
            }
        }
        
        @Override
        public int getItemCount() {
            return 3;
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
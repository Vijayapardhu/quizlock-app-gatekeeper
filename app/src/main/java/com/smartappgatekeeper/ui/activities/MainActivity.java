package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.fragments.DashboardFragment;
import com.smartappgatekeeper.ui.fragments.RoadmapFragment;
import com.smartappgatekeeper.ui.fragments.StoreFragment;
import com.smartappgatekeeper.ui.fragments.ReportsFragment;
import com.smartappgatekeeper.ui.fragments.SettingsFragment;
import com.smartappgatekeeper.ui.fragments.SocialFragment;
import com.smartappgatekeeper.utils.DatabaseUtils;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.service.FloatingAIService;

/**
 * Main Activity with bottom navigation
 * Central hub for all app functionality
 */
public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    
    private BottomNavigationView bottomNavigation;
    private ImageButton buttonSettings;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Reset database if needed to handle schema conflicts
        DatabaseUtils.resetDatabaseIfNeeded(this);
        
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupBottomNavigation();
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        buttonSettings = findViewById(R.id.button_settings);
        
        // Set up settings button click listener
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SettingsFragment());
            }
        });
    }
    
    /**
     * Setup bottom navigation
     */
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(this);
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        
        int itemId = item.getItemId();
        if (itemId == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.nav_roadmap) {
            fragment = new RoadmapFragment();
        } else if (itemId == R.id.nav_store) {
            fragment = new StoreFragment();
        } else if (itemId == R.id.nav_reports) {
            fragment = new ReportsFragment();
        } else if (itemId == R.id.nav_social) {
            fragment = new SocialFragment();
        }
        
        if (fragment != null) {
            loadFragment(fragment);
            return true;
        }
        
        return false;
    }
    
    /**
     * Load fragment into container
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Check if onboarding is completed
        checkOnboardingStatus();
        // Start floating AI service
        startFloatingAI();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Stop floating AI service when app is paused
        stopFloatingAI();
    }
    
    /**
     * Check if user has completed onboarding
     */
    private void checkOnboardingStatus() {
        // Check AppSettings for onboarding completion
        AppRepository repository = AppRepository.getInstance(getApplication());
        repository.getAppSettings().observe(this, settings -> {
            if (settings != null && !settings.onboardingCompleted) {
                // Launch onboarding if not completed
                Intent onboardingIntent = new Intent(this, OnboardingActivity.class);
                startActivity(onboardingIntent);
                finish(); // Close MainActivity
            }
        });
    }
    
    /**
     * Start floating AI service
     */
    private void startFloatingAI() {
        Intent floatingIntent = new Intent(this, FloatingAIService.class);
        floatingIntent.setAction("SHOW_FLOATING_AI");
        startService(floatingIntent);
    }
    
    /**
     * Stop floating AI service
     */
    private void stopFloatingAI() {
        Intent floatingIntent = new Intent(this, FloatingAIService.class);
        floatingIntent.setAction("HIDE_FLOATING_AI");
        startService(floatingIntent);
    }
}

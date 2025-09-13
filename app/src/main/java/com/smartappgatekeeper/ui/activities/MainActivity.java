package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

/**
 * Main Activity with bottom navigation
 * Central hub for all app functionality
 */
public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    
    private BottomNavigationView bottomNavigation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        } else if (itemId == R.id.nav_settings) {
            fragment = new SettingsFragment();
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
    }
    
    /**
     * Check if user has completed onboarding
     */
    private void checkOnboardingStatus() {
        // TODO: Check AppSettings for onboarding completion
        // If not completed, launch OnboardingActivity
    }
}

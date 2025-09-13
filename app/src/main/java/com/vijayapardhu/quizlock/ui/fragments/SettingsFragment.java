package com.vijayapardhu.quizlock.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vijayapardhu.quizlock.databinding.FragmentSettingsBinding;
import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.UserPreferencesDao;
import com.vijayapardhu.quizlock.db.UserPreferences;
import com.vijayapardhu.quizlock.utils.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsFragment extends Fragment {
    
    private FragmentSettingsBinding binding;
    private UserPreferencesDao userPreferencesDao;
    private ExecutorService executor;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize database
        QuizLockDatabase database = QuizLockDatabase.getDatabase(requireContext());
        userPreferencesDao = database.userPreferencesDao();
        executor = Executors.newSingleThreadExecutor();
        
        setupClickListeners();
        loadPreferences();
    }
    
    private void setupClickListeners() {
        binding.btnEnableAccessibility.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });
        
        binding.btnChangeTopics.setOnClickListener(v -> {
            // TODO: Open topic selection dialog
            android.widget.Toast.makeText(getContext(), "Topic selection coming soon", android.widget.Toast.LENGTH_SHORT).show();
        });
        
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference(Constants.PREF_NOTIFICATIONS_ENABLED, String.valueOf(isChecked));
        });
        
        // Save API key when text changes
        binding.etApiKey.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String apiKey = binding.etApiKey.getText().toString().trim();
                savePreference(Constants.PREF_GEMINI_API_KEY, apiKey);
            }
        });
    }
    
    private void loadPreferences() {
        executor.execute(() -> {
            // Load selected topics
            String selectedTopics = userPreferencesDao.getPreference(Constants.PREF_SELECTED_TOPICS);
            if (selectedTopics != null) {
                requireActivity().runOnUiThread(() -> {
                    binding.tvSelectedTopics.setText(selectedTopics.replace(",", ", "));
                });
            }
            
            // Load notifications setting
            String notificationsEnabled = userPreferencesDao.getPreference(Constants.PREF_NOTIFICATIONS_ENABLED);
            if (notificationsEnabled != null) {
                requireActivity().runOnUiThread(() -> {
                    binding.switchNotifications.setChecked(Boolean.parseBoolean(notificationsEnabled));
                });
            }
            
            // Load API key
            String apiKey = userPreferencesDao.getPreference(Constants.PREF_GEMINI_API_KEY);
            if (apiKey != null && !apiKey.isEmpty()) {
                requireActivity().runOnUiThread(() -> {
                    binding.etApiKey.setText(apiKey);
                });
            }
        });
    }
    
    private void savePreference(String key, String value) {
        executor.execute(() -> {
            userPreferencesDao.insertPreference(new UserPreferences(key, value));
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executor != null) {
            executor.shutdown();
        }
        binding = null;
    }
}
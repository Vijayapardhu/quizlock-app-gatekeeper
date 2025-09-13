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

import com.vijayapardhu.quizlock.databinding.FragmentAppsBinding;
import com.vijayapardhu.quizlock.ui.adapters.RestrictedAppsAdapter;
import com.vijayapardhu.quizlock.viewmodel.DashboardViewModel;

public class AppsFragment extends Fragment {
    
    private FragmentAppsBinding binding;
    private DashboardViewModel viewModel;
    private RestrictedAppsAdapter appsAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentAppsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        
        setupRecyclerView();
        observeViewModel();
        setupClickListeners();
    }
    
    private void setupRecyclerView() {
        appsAdapter = new RestrictedAppsAdapter();
        binding.recyclerApps.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerApps.setAdapter(appsAdapter);
    }
    
    private void observeViewModel() {
        viewModel.getRestrictedApps().observe(getViewLifecycleOwner(), restrictedApps -> {
            if (restrictedApps != null) {
                appsAdapter.updateApps(restrictedApps);
            }
        });
    }
    
    private void setupClickListeners() {
        binding.fabAddApp.setOnClickListener(v -> {
            // TODO: Open app selection dialog
            // For now, just show a toast
            android.widget.Toast.makeText(getContext(), "App selection coming soon", android.widget.Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
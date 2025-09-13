package com.quizlock.gatekeeper.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.quizlock.gatekeeper.data.repository.QuizRepository;

/**
 * Factory for creating MainViewModel with dependencies
 */
public class MainViewModelFactory implements ViewModelProvider.Factory {
    
    private final QuizRepository repository;
    
    public MainViewModelFactory(QuizRepository repository) {
        this.repository = repository;
    }
    
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
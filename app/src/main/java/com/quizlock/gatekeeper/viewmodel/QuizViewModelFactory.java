package com.quizlock.gatekeeper.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.quizlock.gatekeeper.data.repository.QuizRepository;

/**
 * Factory for creating QuizViewModel with dependencies
 */
public class QuizViewModelFactory implements ViewModelProvider.Factory {
    
    private final QuizRepository repository;
    
    public QuizViewModelFactory(QuizRepository repository) {
        this.repository = repository;
    }
    
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(QuizViewModel.class)) {
            return (T) new QuizViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
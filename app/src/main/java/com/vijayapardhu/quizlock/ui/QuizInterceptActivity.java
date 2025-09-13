package com.vijayapardhu.quizlock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vijayapardhu.quizlock.R;
import com.vijayapardhu.quizlock.databinding.ActivityQuizInterceptBinding;
import com.vijayapardhu.quizlock.db.QuizQuestion;
import com.vijayapardhu.quizlock.utils.AppUtils;
import com.vijayapardhu.quizlock.utils.Constants;
import com.vijayapardhu.quizlock.viewmodel.QuizViewModel;

public class QuizInterceptActivity extends AppCompatActivity {
    
    private ActivityQuizInterceptBinding binding;
    private QuizViewModel viewModel;
    private String packageName;
    private String appName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizInterceptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Get intent data
        packageName = getIntent().getStringExtra(Constants.EXTRA_PACKAGE_NAME);
        appName = getIntent().getStringExtra(Constants.EXTRA_APP_NAME);
        
        if (packageName == null) {
            finish();
            return;
        }
        
        if (appName == null) {
            appName = AppUtils.getAppName(this, packageName);
        }
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        
        setupUI();
        observeViewModel();
        
        // Load question for the intercepted app
        viewModel.loadQuestionForApp(packageName);
    }
    
    private void setupUI() {
        // Set subtitle with app name
        binding.tvQuizSubtitle.setText(getString(R.string.quiz_subtitle, appName));
        
        // Setup radio button listeners
        binding.radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            binding.btnSubmitAnswer.setEnabled(checkedId != -1);
        });
        
        // Setup button listeners
        binding.btnSubmitAnswer.setOnClickListener(v -> submitAnswer());
        binding.btnEmergencyUnlock.setOnClickListener(v -> emergencyUnlock());
    }
    
    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressLoading.setVisibility(android.view.View.VISIBLE);
                binding.tvQuestion.setVisibility(android.view.View.GONE);
                binding.radioGroupOptions.setVisibility(android.view.View.GONE);
            } else {
                binding.progressLoading.setVisibility(android.view.View.GONE);
                binding.tvQuestion.setVisibility(android.view.View.VISIBLE);
                binding.radioGroupOptions.setVisibility(android.view.View.VISIBLE);
            }
        });
        
        viewModel.getCurrentQuestion().observe(this, this::displayQuestion);
        
        viewModel.getAnswerCorrect().observe(this, isCorrect -> {
            if (isCorrect != null) {
                handleAnswerResult(isCorrect);
            }
        });
        
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        
        viewModel.getCooldownSeconds().observe(this, cooldownSeconds -> {
            if (cooldownSeconds > 0) {
                binding.tvCooldown.setVisibility(android.view.View.VISIBLE);
                binding.tvCooldown.setText("Wait " + (cooldownSeconds / 60) + " minutes before trying again");
                binding.btnSubmitAnswer.setEnabled(false);
            } else {
                binding.tvCooldown.setVisibility(android.view.View.GONE);
            }
        });
    }
    
    private void displayQuestion(QuizQuestion question) {
        if (question == null) return;
        
        binding.tvQuestion.setText(question.getQuestion());
        binding.radioOptionA.setText("A) " + question.getOptionA());
        binding.radioOptionB.setText("B) " + question.getOptionB());
        binding.radioOptionC.setText("C) " + question.getOptionC());
        binding.radioOptionD.setText("D) " + question.getOptionD());
        
        // Clear any previous selection
        binding.radioGroupOptions.clearCheck();
        binding.btnSubmitAnswer.setEnabled(false);
    }
    
    private void submitAnswer() {
        int selectedId = binding.radioGroupOptions.getCheckedRadioButtonId();
        String selectedAnswer = "";
        
        if (selectedId == R.id.radioOptionA) {
            selectedAnswer = "A";
        } else if (selectedId == R.id.radioOptionB) {
            selectedAnswer = "B";
        } else if (selectedId == R.id.radioOptionC) {
            selectedAnswer = "C";
        } else if (selectedId == R.id.radioOptionD) {
            selectedAnswer = "D";
        }
        
        if (!selectedAnswer.isEmpty()) {
            viewModel.submitAnswer(selectedAnswer);
        }
    }
    
    private void handleAnswerResult(boolean isCorrect) {
        if (isCorrect) {
            Toast.makeText(this, R.string.quiz_correct, Toast.LENGTH_SHORT).show();
            
            // Start timer activity and unlock the app
            Intent timerIntent = new Intent(this, TimerActivity.class);
            timerIntent.putExtra(Constants.EXTRA_PACKAGE_NAME, packageName);
            timerIntent.putExtra(Constants.EXTRA_APP_NAME, appName);
            startActivity(timerIntent);
            
            finish();
        } else {
            Toast.makeText(this, R.string.quiz_incorrect, Toast.LENGTH_SHORT).show();
            // Stay on this screen for retry or cooldown
        }
    }
    
    private void emergencyUnlock() {
        viewModel.emergencyUnlock();
        
        // Start timer activity with emergency unlock flag
        Intent timerIntent = new Intent(this, TimerActivity.class);
        timerIntent.putExtra(Constants.EXTRA_PACKAGE_NAME, packageName);
        timerIntent.putExtra(Constants.EXTRA_APP_NAME, appName);
        timerIntent.putExtra("emergency_unlock", true);
        startActivity(timerIntent);
        
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // Prevent going back without answering or emergency unlock
        // Do nothing - user must answer or use emergency unlock
    }
}
package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.smartappgatekeeper.R;
import com.smartappgatekeeper.viewmodel.OnboardingViewModel;
import com.smartappgatekeeper.database.entities.TargetApp;
import java.util.List;

/**
 * Onboarding Activity
 * Guides new users through the initial setup process
 */
public class OnboardingActivity extends AppCompatActivity {
    
    private OnboardingViewModel viewModel;
    private int currentStep = 0;
    
    // UI Components
    private View stepWelcome, stepUserInfo, stepGoal, stepApps, stepComplete;
    private Button btnNext, btnPrevious, btnSkip, btnComplete;
    private EditText etName, etEmail, etAge;
    private RadioGroup rgGoal;
    private Spinner spLanguage;
    private TextView tvStepTitle, tvStepDescription;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        
        initViews();
        setupViewModel();
        setupClickListeners();
        showStep(0);
    }
    
    /**
     * Initialize UI components
     */
    private void initViews() {
        // Only initialize elements that exist in the layout
        btnNext = findViewById(R.id.btn_next);
        spLanguage = findViewById(R.id.sp_language);
        
        // Initialize other variables as null for now
        stepWelcome = null;
        stepUserInfo = null;
        stepGoal = null;
        stepApps = null;
        stepComplete = null;
        btnPrevious = null;
        btnSkip = null;
        btnComplete = null;
        etName = null;
        etEmail = null;
        etAge = null;
        rgGoal = null;
        tvStepTitle = null;
        tvStepDescription = null;
    }
    
    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);
        
        // Observe ViewModel data
        viewModel.getCurrentStep().observe(this, this::updateStep);
        viewModel.getIsOnboardingComplete().observe(this, this::onOnboardingComplete);
        viewModel.getErrorMessage().observe(this, this::showError);
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (isCurrentStepValid()) {
                    saveCurrentStepData();
                    viewModel.nextStep();
                }
            });
        }
        
        if (btnPrevious != null) {
            btnPrevious.setOnClickListener(v -> viewModel.previousStep());
        }
        
        if (btnSkip != null) {
            btnSkip.setOnClickListener(v -> viewModel.skipOnboarding());
        }
        
        if (btnComplete != null) {
            btnComplete.setOnClickListener(v -> viewModel.completeOnboarding());
        }
    }
    
    /**
     * Update current step UI
     */
    private void updateStep(Integer step) {
        if (step != null) {
            currentStep = step;
            showStep(step);
        }
    }
    
    /**
     * Show specific step
     */
    private void showStep(int step) {
        // Hide all steps
        stepWelcome.setVisibility(View.GONE);
        stepUserInfo.setVisibility(View.GONE);
        stepGoal.setVisibility(View.GONE);
        stepApps.setVisibility(View.GONE);
        stepComplete.setVisibility(View.GONE);
        
        // Show current step
        switch (step) {
            case 0:
                showWelcomeStep();
                break;
            case 1:
                showUserInfoStep();
                break;
            case 2:
                showGoalStep();
                break;
            case 3:
                showAppsStep();
                break;
            case 4:
                showCompleteStep();
                break;
        }
        
        // Update button visibility
        updateButtonVisibility(step);
    }
    
    /**
     * Show welcome step
     */
    private void showWelcomeStep() {
        stepWelcome.setVisibility(View.VISIBLE);
        tvStepTitle.setText("Welcome to Smart App Gatekeeper!");
        tvStepDescription.setText("Let's set up your account and preferences to get started.");
    }
    
    /**
     * Show user info step
     */
    private void showUserInfoStep() {
        stepUserInfo.setVisibility(View.VISIBLE);
        tvStepTitle.setText("Tell us about yourself");
        tvStepDescription.setText("This helps us personalize your experience.");
    }
    
    /**
     * Show goal step
     */
    private void showGoalStep() {
        stepGoal.setVisibility(View.VISIBLE);
        tvStepTitle.setText("What's your goal?");
        tvStepDescription.setText("Choose what you want to achieve with our app.");
    }
    
    /**
     * Show apps step
     */
    private void showAppsStep() {
        stepApps.setVisibility(View.VISIBLE);
        tvStepTitle.setText("Select apps to monitor");
        tvStepDescription.setText("Choose which apps you want to control with quizzes.");
    }
    
    /**
     * Show complete step
     */
    private void showCompleteStep() {
        stepComplete.setVisibility(View.VISIBLE);
        tvStepTitle.setText("You're all set!");
        tvStepDescription.setText("Welcome to Smart App Gatekeeper. Let's start your journey!");
    }
    
    /**
     * Update button visibility based on step
     */
    private void updateButtonVisibility(int step) {
        if (btnPrevious != null) {
            btnPrevious.setVisibility(step > 0 ? View.VISIBLE : View.GONE);
        }
        if (btnNext != null) {
            btnNext.setVisibility(step < 4 ? View.VISIBLE : View.GONE);
        }
        if (btnSkip != null) {
            btnSkip.setVisibility(step < 4 ? View.VISIBLE : View.GONE);
        }
        if (btnComplete != null) {
            btnComplete.setVisibility(step == 4 ? View.VISIBLE : View.GONE);
        }
    }
    
    /**
     * Check if current step is valid
     */
    private boolean isCurrentStepValid() {
        switch (currentStep) {
            case 0: // Welcome step
                return true;
            case 1: // User info step
                return etName != null && !etName.getText().toString().trim().isEmpty();
            case 2: // Goal step
                return rgGoal != null && rgGoal.getCheckedRadioButtonId() != -1;
            case 3: // Apps step
                return true; // Apps are optional
            case 4: // Complete step
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Save current step data
     */
    private void saveCurrentStepData() {
        switch (currentStep) {
            case 1: // User info step
                if (etName != null) {
                    viewModel.setUserName(etName.getText().toString().trim());
                }
                if (etEmail != null) {
                    viewModel.setUserEmail(etEmail.getText().toString().trim());
                }
                if (etAge != null) {
                    try {
                        int age = Integer.parseInt(etAge.getText().toString());
                        viewModel.setUserAge(age);
                    } catch (NumberFormatException e) {
                        viewModel.setUserAge(25); // Default age
                    }
                }
                break;
            case 2: // Goal step
                if (rgGoal != null) {
                    int selectedGoalId = rgGoal.getCheckedRadioButtonId();
                    String goal = getGoalFromRadioButton(selectedGoalId);
                    viewModel.setSelectedGoal(goal);
                }
                break;
            case 3: // Apps step
                // App selection is handled by individual app toggles
                break;
        }
    }
    
    /**
     * Get goal from radio button selection
     */
    private String getGoalFromRadioButton(int radioButtonId) {
        // Since radio buttons don't exist in the simple layout, return default
        return "Reduce screen time"; // Default
    }
    
    /**
     * Handle onboarding completion
     */
    private void onOnboardingComplete(Boolean isComplete) {
        if (isComplete != null && isComplete) {
            // Navigate to main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String error) {
        if (error != null && !error.isEmpty()) {
            // Show error dialog or toast
            // Implementation depends on UI preference
        }
    }
    
    /**
     * Handle back button press
     */
    @Override
    public void onBackPressed() {
        if (currentStep > 0) {
            viewModel.previousStep();
        } else {
            super.onBackPressed();
        }
    }
}

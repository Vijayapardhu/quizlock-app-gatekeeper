package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.smartappgatekeeper.R;
import com.smartappgatekeeper.viewmodel.QuizViewModel;
import com.smartappgatekeeper.model.Question;

/**
 * Quiz Activity
 * Displays quiz questions and handles user interactions
 */
public class QuizActivity extends AppCompatActivity {
    
    private QuizViewModel viewModel;
    private CountDownTimer timer;
    
    // UI Components
    private TextView tvQuestion, tvQuestionNumber, tvTimeRemaining, tvScore;
    private RadioGroup rgAnswers;
    private RadioButton rbAnswerA, rbAnswerB, rbAnswerC, rbAnswerD;
    private Button btnSubmit, btnSkip, btnPause, btnResume;
    private ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        initViews();
        setupViewModel();
        setupClickListeners();
        startQuiz();
    }
    
    /**
     * Initialize UI components
     */
    private void initViews() {
        tvQuestion = findViewById(R.id.tv_question);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvTimeRemaining = findViewById(R.id.tv_time_remaining);
        tvScore = findViewById(R.id.tv_score);
        
        rgAnswers = findViewById(R.id.rg_answers);
        rbAnswerA = findViewById(R.id.rb_answer_a);
        rbAnswerB = findViewById(R.id.rb_answer_b);
        rbAnswerC = findViewById(R.id.rb_answer_c);
        rbAnswerD = findViewById(R.id.rb_answer_d);
        
        btnSubmit = findViewById(R.id.btn_submit);
        btnSkip = findViewById(R.id.btn_skip);
        btnPause = findViewById(R.id.btn_pause);
        btnResume = findViewById(R.id.btn_resume);
        
        progressBar = findViewById(R.id.progress_bar);
    }
    
    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        
        // Observe ViewModel data
        viewModel.getCurrentQuestion().observe(this, this::displayQuestion);
        viewModel.getCurrentQuestionIndex().observe(this, this::updateQuestionNumber);
        viewModel.getScore().observe(this, this::updateScore);
        viewModel.getTimeRemaining().observe(this, time -> updateTimeRemaining(time.intValue()));
        viewModel.getIsQuizActive().observe(this, this::updateQuizState);
        viewModel.getIsQuizCompleted().observe(this, this::onQuizCompleted);
        viewModel.getErrorMessage().observe(this, this::showError);
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        btnSubmit.setOnClickListener(v -> submitAnswer());
        btnSkip.setOnClickListener(v -> viewModel.skipQuestion());
        btnPause.setOnClickListener(v -> viewModel.pauseQuiz());
        btnResume.setOnClickListener(v -> viewModel.resumeQuiz());
        
        // Answer selection
        rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedAnswer = getSelectedAnswer();
            if (selectedAnswer != null) {
                btnSubmit.setEnabled(true);
            }
        });
    }
    
    /**
     * Start the quiz
     */
    private void startQuiz() {
        viewModel.startQuiz();
    }
    
    /**
     * Display current question
     */
    private void displayQuestion(Question question) {
        if (question != null) {
            tvQuestion.setText(question.getQuestionText());
            rbAnswerA.setText("A. " + question.getOptionA());
            rbAnswerB.setText("B. " + question.getOptionB());
            rbAnswerC.setText("C. " + question.getOptionC());
            rbAnswerD.setText("D. " + question.getOptionD());
            
            // Clear previous selection
            rgAnswers.clearCheck();
            btnSubmit.setEnabled(false);
        }
    }
    
    /**
     * Update question number
     */
    private void updateQuestionNumber(Integer questionIndex) {
        if (questionIndex != null) {
            int questionNumber = questionIndex + 1;
            tvQuestionNumber.setText("Question " + questionNumber);
            
            // Update progress bar
            int progress = viewModel.getProgressPercentage();
            progressBar.setProgress(progress);
        }
    }
    
    /**
     * Update score display
     */
    private void updateScore(Integer score) {
        if (score != null) {
            tvScore.setText("Score: " + score);
        }
    }
    
    /**
     * Update time remaining
     */
    private void updateTimeRemaining(Integer timeRemaining) {
        if (timeRemaining != null) {
            tvTimeRemaining.setText("Time: " + formatTime(timeRemaining));
        }
    }
    
    /**
     * Update quiz state
     */
    private void updateQuizState(Boolean isActive) {
        if (isActive != null) {
            btnPause.setVisibility(isActive ? View.VISIBLE : View.GONE);
            btnResume.setVisibility(isActive ? View.GONE : View.VISIBLE);
            btnSubmit.setEnabled(isActive);
            btnSkip.setEnabled(isActive);
        }
    }
    
    /**
     * Handle quiz completion
     */
    private void onQuizCompleted(Boolean isCompleted) {
        if (isCompleted != null && isCompleted) {
            // Stop timer
            if (timer != null) {
                timer.cancel();
            }
            
            // Show completion dialog or navigate to results
            showQuizCompletionDialog();
        }
    }
    
    /**
     * Submit answer
     */
    private void submitAnswer() {
        String selectedAnswer = getSelectedAnswer();
        if (selectedAnswer != null) {
            viewModel.submitAnswer(selectedAnswer);
        }
    }
    
    /**
     * Get selected answer
     */
    private String getSelectedAnswer() {
        int checkedId = rgAnswers.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_answer_a) {
            return "A";
        } else if (checkedId == R.id.rb_answer_b) {
            return "B";
        } else if (checkedId == R.id.rb_answer_c) {
            return "C";
        } else if (checkedId == R.id.rb_answer_d) {
            return "D";
        }
        return null;
    }
    
    /**
     * Format time in seconds to MM:SS format
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    
    /**
     * Show quiz completion dialog
     */
    private void showQuizCompletionDialog() {
        // Create completion dialog
        // This would typically show results, achievements, etc.
        // For now, just navigate back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
        // Show confirmation dialog before exiting quiz
        // For now, just call super
        super.onBackPressed();
    }
    
    /**
     * Clean up resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}

package com.quizlock.gatekeeper.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.quizlock.gatekeeper.R;
import com.quizlock.gatekeeper.QuizLockApplication;
import com.quizlock.gatekeeper.data.model.QuizQuestion;
import com.quizlock.gatekeeper.utils.AppInfoHelper;
import com.quizlock.gatekeeper.viewmodel.QuizViewModel;
import com.quizlock.gatekeeper.viewmodel.QuizViewModelFactory;

/**
 * Activity for displaying quiz questions to unlock blocked apps
 */
public class QuizActivity extends AppCompatActivity {
    
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    public static final String EXTRA_APP_NAME = "app_name";
    public static final String EXTRA_CATEGORY = "category";
    
    private static final int QUIZ_TIME_LIMIT_SECONDS = 30;
    
    private QuizViewModel viewModel;
    private CountDownTimer timer;
    private long questionStartTime;
    
    // Views
    private ImageView ivAppIcon;
    private TextView tvQuizSubtitle;
    private TextView tvTimer;
    private TextView tvCategory;
    private TextView tvQuestion;
    private RadioGroup rgOptions;
    private RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD;
    private MaterialButton btnSkip, btnSubmit;
    private LinearLayout llLoading;
    
    // Data
    private String packageName;
    private String appName;
    private String category;
    private QuizQuestion currentQuestion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        extractIntentData();
        initializeViewModel();
        initializeViews();
        setupClickListeners();
        observeViewModel();
        
        loadQuestion();
    }
    
    private void extractIntentData() {
        Intent intent = getIntent();
        packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
        appName = intent.getStringExtra(EXTRA_APP_NAME);
        category = intent.getStringExtra(EXTRA_CATEGORY);
        
        if (packageName == null) {
            finish();
            return;
        }
        
        if (appName == null) {
            appName = AppInfoHelper.getAppName(this, packageName);
        }
        
        if (category == null) {
            category = "general"; // Default category
        }
    }
    
    private void initializeViewModel() {
        QuizLockApplication app = (QuizLockApplication) getApplication();
        QuizViewModelFactory factory = new QuizViewModelFactory(app.getQuizRepository());
        viewModel = new ViewModelProvider(this, factory).get(QuizViewModel.class);
    }
    
    private void initializeViews() {
        ivAppIcon = findViewById(R.id.iv_app_icon);
        tvQuizSubtitle = findViewById(R.id.tv_quiz_subtitle);
        tvTimer = findViewById(R.id.tv_timer);
        tvCategory = findViewById(R.id.tv_category);
        tvQuestion = findViewById(R.id.tv_question);
        rgOptions = findViewById(R.id.rg_options);
        rbOptionA = findViewById(R.id.rb_option_a);
        rbOptionB = findViewById(R.id.rb_option_b);
        rbOptionC = findViewById(R.id.rb_option_c);
        rbOptionD = findViewById(R.id.rb_option_d);
        btnSkip = findViewById(R.id.btn_skip);
        btnSubmit = findViewById(R.id.btn_submit);
        llLoading = findViewById(R.id.ll_loading);
        
        // Set app info
        try {
            ivAppIcon.setImageDrawable(AppInfoHelper.getAppIcon(this, packageName));
        } catch (Exception e) {
            ivAppIcon.setImageResource(R.mipmap.ic_launcher);
        }
        
        tvQuizSubtitle.setText(getString(R.string.quiz_subtitle, appName));
    }
    
    private void setupClickListeners() {
        btnSkip.setOnClickListener(v -> skipQuestion());
        btnSubmit.setOnClickListener(v -> submitAnswer());
        
        // Enable submit button only when an option is selected
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            btnSubmit.setEnabled(checkedId != -1);
        });
    }
    
    private void observeViewModel() {
        viewModel.getQuestionLiveData().observe(this, question -> {
            if (question != null) {
                displayQuestion(question);
            } else {
                showError("Failed to load question");
            }
        });
        
        viewModel.getAnswerResult().observe(this, result -> {
            if (result != null) {
                handleAnswerResult(result);
            }
        });
        
        viewModel.getLoading().observe(this, isLoading -> {
            llLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void loadQuestion() {
        questionStartTime = System.currentTimeMillis();
        viewModel.loadRandomQuestion(category);
        startTimer();
    }
    
    private void displayQuestion(QuizQuestion question) {
        this.currentQuestion = question;
        
        // Set category
        tvCategory.setText(getCategoryDisplayName(question.getCategory()));
        int categoryColor = getCategoryColor(question.getCategory());
        tvCategory.setBackgroundTintList(
            androidx.core.content.ContextCompat.getColorStateList(this, categoryColor)
        );
        
        // Set question and options
        tvQuestion.setText(question.getQuestionText());
        rbOptionA.setText(question.getOptionA());
        rbOptionB.setText(question.getOptionB());
        rbOptionC.setText(question.getOptionC());
        rbOptionD.setText(question.getOptionD());
        
        // Clear previous selection
        rgOptions.clearCheck();
        btnSubmit.setEnabled(false);
    }
    
    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        
        timer = new CountDownTimer(QUIZ_TIME_LIMIT_SECONDS * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(secondsLeft));
                
                // Change color as time runs out
                if (secondsLeft <= 10) {
                    tvTimer.setBackgroundTintList(
                        androidx.core.content.ContextCompat.getColorStateList(QuizActivity.this, R.color.error)
                    );
                }
            }
            
            @Override
            public void onFinish() {
                timeUp();
            }
        };
        
        timer.start();
    }
    
    private void submitAnswer() {
        if (currentQuestion == null) return;
        
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId == -1) return;
        
        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();
        
        long timeTaken = System.currentTimeMillis() - questionStartTime;
        
        if (timer != null) {
            timer.cancel();
        }
        
        viewModel.submitAnswer(currentQuestion, selectedAnswer, timeTaken, packageName);
    }
    
    private void skipQuestion() {
        if (timer != null) {
            timer.cancel();
        }
        
        // Load another question
        loadQuestion();
    }
    
    private void timeUp() {
        Toast.makeText(this, "Time's up! Try again.", Toast.LENGTH_SHORT).show();
        loadQuestion(); // Load new question
    }
    
    private void handleAnswerResult(QuizViewModel.AnswerResult result) {
        if (result.isCorrect()) {
            showSuccessAndFinish();
        } else {
            showIncorrectAnswer();
        }
    }
    
    private void showSuccessAndFinish() {
        Toast.makeText(this, getString(R.string.correct_answer, 15), Toast.LENGTH_LONG).show();
        
        // Unlock the app
        viewModel.unlockApp(packageName);
        
        // Finish with success result
        setResult(RESULT_OK);
        finish();
        
        // Apply exit animation
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_exit);
    }
    
    private void showIncorrectAnswer() {
        Toast.makeText(this, R.string.incorrect_answer, Toast.LENGTH_SHORT).show();
        
        // Load new question after a short delay
        tvTimer.postDelayed(this::loadQuestion, 1500);
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
    
    private String getCategoryDisplayName(String category) {
        switch (category.toLowerCase()) {
            case "math":
                return getString(R.string.category_math);
            case "coding":
                return getString(R.string.category_coding);
            case "general":
                return getString(R.string.category_general);
            default:
                return category;
        }
    }
    
    private int getCategoryColor(String category) {
        switch (category.toLowerCase()) {
            case "math":
                return R.color.category_math;
            case "coding":
                return R.color.category_coding;
            case "general":
                return R.color.category_general;
            default:
                return R.color.primary;
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
    
    @Override
    public void onBackPressed() {
        // Prevent back button during quiz
        // User must answer or skip to exit
    }
}
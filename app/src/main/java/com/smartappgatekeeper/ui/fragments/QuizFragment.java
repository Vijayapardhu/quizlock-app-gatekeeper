package com.smartappgatekeeper.ui.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.model.Question;
import com.smartappgatekeeper.viewmodel.QuizViewModel;

import java.util.List;

/**
 * Quiz Fragment - Interactive quiz system with scoring
 * FR-004: System shall log all interception events for analytics
 * FR-008: System shall track quiz performance and accuracy
 */
public class QuizFragment extends Fragment {
    
    private QuizViewModel viewModel;
    
    // UI Views
    private TextView textQuestion;
    private TextView textQuestionNumber;
    private TextView textScore;
    private TextView textTimeLeft;
    private TextView textDifficulty;
    private TextView textTopic;
    
    private Button buttonOptionA;
    private Button buttonOptionB;
    private Button buttonOptionC;
    private Button buttonOptionD;
    private Button buttonNext;
    private Button buttonHint;
    private Button buttonSkip;
    
    private ProgressBar progressQuiz;
    private ProgressBar progressTime;
    
    private CountDownTimer timer;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private boolean isQuizActive = false;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        initializeViews(view);
        
        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        
        // Setup UI
        setupUI();
        
        // Observe ViewModel
        observeViewModel();
        
        // Start quiz
        startQuiz();
    }
    
    /**
     * Initialize all UI views
     */
    private void initializeViews(View view) {
        // Text views
        textQuestion = view.findViewById(R.id.text_question);
        textQuestionNumber = view.findViewById(R.id.text_question_number);
        textScore = view.findViewById(R.id.text_score);
        textTimeLeft = view.findViewById(R.id.text_time_left);
        textDifficulty = view.findViewById(R.id.text_difficulty);
        textTopic = view.findViewById(R.id.text_topic);
        
        // Buttons
        buttonOptionA = view.findViewById(R.id.button_option_a);
        buttonOptionB = view.findViewById(R.id.button_option_b);
        buttonOptionC = view.findViewById(R.id.button_option_c);
        buttonOptionD = view.findViewById(R.id.button_option_d);
        buttonNext = view.findViewById(R.id.button_next);
        buttonHint = view.findViewById(R.id.button_hint);
        buttonSkip = view.findViewById(R.id.button_skip);
        
        // Progress bars
        progressQuiz = view.findViewById(R.id.progress_quiz);
        progressTime = view.findViewById(R.id.progress_time);
    }
    
    /**
     * Setup UI components
     */
    private void setupUI() {
        // Setup option button click listeners
        buttonOptionA.setOnClickListener(v -> selectAnswer("A"));
        buttonOptionB.setOnClickListener(v -> selectAnswer("B"));
        buttonOptionC.setOnClickListener(v -> selectAnswer("C"));
        buttonOptionD.setOnClickListener(v -> selectAnswer("D"));
        
        // Setup action button click listeners
        buttonNext.setOnClickListener(v -> nextQuestion());
        buttonHint.setOnClickListener(v -> showHint());
        buttonSkip.setOnClickListener(v -> skipQuestion());
        
        // Initialize progress bars
        progressQuiz.setMax(100);
        progressTime.setMax(30); // 30 seconds per question
    }
    
    /**
     * Observe ViewModel LiveData
     */
    private void observeViewModel() {
        viewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            if (questions != null && !questions.isEmpty()) {
                totalQuestions = questions.size();
                displayCurrentQuestion();
            }
        });
        
        viewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), question -> {
            if (question != null) {
                displayQuestion(question);
            }
        });
        
        viewModel.getQuizScore().observe(getViewLifecycleOwner(), quizScore -> {
            score = quizScore;
            updateScoreDisplay();
        });
        
        viewModel.getCorrectAnswers().observe(getViewLifecycleOwner(), correct -> {
            correctAnswers = correct;
            updateScoreDisplay();
        });
    }
    
    /**
     * Start the quiz
     */
    private void startQuiz() {
        isQuizActive = true;
        currentQuestionIndex = 0;
        score = 0;
        correctAnswers = 0;
        
        // Load questions
        viewModel.loadQuestions();
        
        // Update UI
        updateScoreDisplay();
        updateQuestionNumber();
    }
    
    /**
     * Display current question
     */
    private void displayCurrentQuestion() {
        if (currentQuestionIndex < totalQuestions) {
            viewModel.getCurrentQuestion().getValue();
            startQuestionTimer();
        } else {
            finishQuiz();
        }
    }
    
    /**
     * Display question details
     */
    private void displayQuestion(Question question) {
        if (question != null) {
            textQuestion.setText(question.getQuestionText());
            textDifficulty.setText("Difficulty: " + question.getDifficulty());
            textTopic.setText("Topic: " + question.getTopic());
            
            // Set option texts
            buttonOptionA.setText("A) " + question.getOptionA());
            buttonOptionB.setText("B) " + question.getOptionB());
            buttonOptionC.setText("C) " + question.getOptionC());
            buttonOptionD.setText("D) " + question.getOptionD());
            
            // Reset button states
            resetOptionButtons();
            
            // Update question number
            updateQuestionNumber();
        }
    }
    
    /**
     * Select an answer
     */
    private void selectAnswer(String selectedAnswer) {
        if (!isQuizActive) return;
        
        // Stop timer
        if (timer != null) {
            timer.cancel();
        }
        
        // Get current question
        Question currentQuestion = viewModel.getCurrentQuestion().getValue();
        if (currentQuestion != null) {
            boolean isCorrect = selectedAnswer.equals(currentQuestion.getCorrectAnswer());
            
            // Update score
            if (isCorrect) {
                score += 10; // 10 points per correct answer
                correctAnswers++;
                showAnswerFeedback(true, selectedAnswer);
            } else {
                showAnswerFeedback(false, selectedAnswer);
            }
            
            // Update UI
            updateScoreDisplay();
            highlightCorrectAnswer(currentQuestion.getCorrectAnswer());
            disableOptionButtons();
            
            // Show next button
            buttonNext.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Show answer feedback
     */
    private void showAnswerFeedback(boolean isCorrect, String selectedAnswer) {
        String message = isCorrect ? "Correct! +10 points" : "Incorrect! The answer was " + selectedAnswer;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Highlight correct answer
     */
    private void highlightCorrectAnswer(String correctAnswer) {
        // Reset all buttons first
        resetOptionButtons();
        
        // Highlight correct answer
        switch (correctAnswer) {
            case "A":
                buttonOptionA.setBackgroundColor(getResources().getColor(R.color.progress_fill));
                break;
            case "B":
                buttonOptionB.setBackgroundColor(getResources().getColor(R.color.progress_fill));
                break;
            case "C":
                buttonOptionC.setBackgroundColor(getResources().getColor(R.color.progress_fill));
                break;
            case "D":
                buttonOptionD.setBackgroundColor(getResources().getColor(R.color.progress_fill));
                break;
        }
    }
    
    /**
     * Next question
     */
    private void nextQuestion() {
        currentQuestionIndex++;
        
        if (currentQuestionIndex < totalQuestions) {
            displayCurrentQuestion();
            buttonNext.setVisibility(View.GONE);
        } else {
            finishQuiz();
        }
    }
    
    /**
     * Show hint
     */
    private void showHint() {
        Question currentQuestion = viewModel.getCurrentQuestion().getValue();
        if (currentQuestion != null && currentQuestion.getExplanation() != null) {
            Toast.makeText(getContext(), "Hint: " + currentQuestion.getExplanation(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "No hint available for this question", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Skip question
     */
    private void skipQuestion() {
        if (timer != null) {
            timer.cancel();
        }
        
        // Deduct points for skipping
        score = Math.max(0, score - 5);
        updateScoreDisplay();
        
        nextQuestion();
    }
    
    /**
     * Start question timer
     */
    private void startQuestionTimer() {
        if (timer != null) {
            timer.cancel();
        }
        
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                textTimeLeft.setText(secondsLeft + "s");
                progressTime.setProgress(secondsLeft);
            }
            
            @Override
            public void onFinish() {
                // Time's up - auto skip
                textTimeLeft.setText("Time's up!");
                skipQuestion();
            }
        };
        
        timer.start();
    }
    
    /**
     * Update score display
     */
    private void updateScoreDisplay() {
        textScore.setText("Score: " + score);
        
        // Update quiz progress
        if (totalQuestions > 0) {
            int progress = (currentQuestionIndex * 100) / totalQuestions;
            progressQuiz.setProgress(progress);
        }
    }
    
    /**
     * Update question number
     */
    private void updateQuestionNumber() {
        textQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + totalQuestions);
    }
    
    /**
     * Reset option buttons
     */
    private void resetOptionButtons() {
        buttonOptionA.setBackgroundColor(getResources().getColor(R.color.bg_gradient_start));
        buttonOptionB.setBackgroundColor(getResources().getColor(R.color.bg_gradient_start));
        buttonOptionC.setBackgroundColor(getResources().getColor(R.color.bg_gradient_start));
        buttonOptionD.setBackgroundColor(getResources().getColor(R.color.bg_gradient_start));
        
        buttonOptionA.setEnabled(true);
        buttonOptionB.setEnabled(true);
        buttonOptionC.setEnabled(true);
        buttonOptionD.setEnabled(true);
    }
    
    /**
     * Disable option buttons
     */
    private void disableOptionButtons() {
        buttonOptionA.setEnabled(false);
        buttonOptionB.setEnabled(false);
        buttonOptionC.setEnabled(false);
        buttonOptionD.setEnabled(false);
    }
    
    /**
     * Finish quiz
     */
    private void finishQuiz() {
        isQuizActive = false;
        
        if (timer != null) {
            timer.cancel();
        }
        
        // Calculate final score
        double accuracy = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        
        // Show results
        String resultMessage = String.format(
            "Quiz Complete!\nScore: %d\nCorrect: %d/%d\nAccuracy: %.1f%%",
            score, correctAnswers, totalQuestions, accuracy
        );
        
        Toast.makeText(getContext(), resultMessage, Toast.LENGTH_LONG).show();
        
        // Save quiz results
        viewModel.saveQuizResults(score, correctAnswers, totalQuestions, accuracy);
        
        // Navigate back or show results
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}

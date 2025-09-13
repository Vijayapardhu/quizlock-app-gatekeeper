package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.Question;
import com.smartappgatekeeper.database.entities.UsageEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * ViewModel for QuizActivity
 * Manages quiz logic, questions, and scoring
 */
public class QuizViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    private final MutableLiveData<List<Question>> questions = new MutableLiveData<>();
    private final MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentQuestionIndex = new MutableLiveData<>();
    private final MutableLiveData<Integer> score = new MutableLiveData<>();
    private final MutableLiveData<Integer> timeRemaining = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isQuizActive = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isQuizCompleted = new MutableLiveData<>();
    private final MutableLiveData<String> selectedAnswer = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    private List<Question> quizQuestions;
    private int currentIndex = 0;
    private int currentScore = 0;
    private int quizTimeLimit = 300; // 5 minutes in seconds
    private Random random = new Random();
    
    public QuizViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        score.setValue(0);
        currentQuestionIndex.setValue(0);
        isQuizActive.setValue(false);
        isQuizCompleted.setValue(false);
        loadQuestions();
    }
    
    /**
     * Load questions for the quiz
     */
    private void loadQuestions() {
        repository.getAllQuestions().observeForever(questionList -> {
            if (questionList != null && !questionList.isEmpty()) {
                // Select random questions for the quiz
                List<Question> selectedQuestions = selectRandomQuestions(questionList, 10);
                questions.setValue(selectedQuestions);
                quizQuestions = selectedQuestions;
            } else {
                // Create mock questions if none exist
                createMockQuestions();
            }
        });
    }
    
    /**
     * Select random questions for the quiz
     */
    private List<Question> selectRandomQuestions(List<Question> allQuestions, int count) {
        List<Question> shuffled = new ArrayList<>(allQuestions);
        Collections.shuffle(shuffled);
        return shuffled.size() > count ? shuffled.subList(0, count) : shuffled;
    }
    
    /**
     * Create mock questions for testing
     */
    private void createMockQuestions() {
        List<Question> mockQuestions = new ArrayList<>();
        
        mockQuestions.add(new Question(
            "What is the primary purpose of this app?",
            "To help reduce screen time through gamified learning",
            "To increase social media usage",
            "To play games",
            "To browse the internet",
            "A"
        ));
        
        mockQuestions.add(new Question(
            "How many minutes do you get for answering a question correctly?",
            "1 minute",
            "2 minutes",
            "5 minutes",
            "10 minutes",
            "B"
        ));
        
        mockQuestions.add(new Question(
            "What happens when you maintain a streak?",
            "Nothing",
            "You earn coins",
            "You lose points",
            "The app stops working",
            "B"
        ));
        
        mockQuestions.add(new Question(
            "Which of the following is NOT a benefit of reducing screen time?",
            "Better sleep quality",
            "Improved focus",
            "Increased anxiety",
            "More free time",
            "C"
        ));
        
        mockQuestions.add(new Question(
            "What is the recommended daily screen time for adults?",
            "2-3 hours",
            "4-6 hours",
            "8-10 hours",
            "12+ hours",
            "A"
        ));
        
        questions.setValue(mockQuestions);
        quizQuestions = mockQuestions;
    }
    
    /**
     * Start the quiz
     */
    public void startQuiz() {
        if (quizQuestions != null && !quizQuestions.isEmpty()) {
            currentIndex = 0;
            currentScore = 0;
            score.setValue(0);
            currentQuestionIndex.setValue(0);
            isQuizActive.setValue(true);
            isQuizCompleted.setValue(false);
            
            // Start timer
            startTimer();
            
            // Load first question
            loadCurrentQuestion();
        } else {
            errorMessage.setValue("No questions available for the quiz");
        }
    }
    
    /**
     * Load current question
     */
    private void loadCurrentQuestion() {
        if (quizQuestions != null && currentIndex < quizQuestions.size()) {
            currentQuestion.setValue(quizQuestions.get(currentIndex));
            selectedAnswer.setValue("");
        }
    }
    
    /**
     * Start quiz timer
     */
    private void startTimer() {
        timeRemaining.setValue(quizTimeLimit);
        
        CompletableFuture.runAsync(() -> {
            for (int i = quizTimeLimit; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                    timeRemaining.postValue(i);
                } catch (InterruptedException e) {
                    break;
                }
            }
            
            // Time's up
            if (isQuizActive.getValue() != null && isQuizActive.getValue()) {
                completeQuiz();
            }
        });
    }
    
    /**
     * Submit answer for current question
     */
    public void submitAnswer(String answer) {
        if (isQuizActive.getValue() != null && isQuizActive.getValue()) {
            selectedAnswer.setValue(answer);
            
            Question question = currentQuestion.getValue();
            if (question != null && question.getCorrectAnswer().equals(answer)) {
                currentScore += 10; // 10 points per correct answer
                score.setValue(currentScore);
            }
            
            // Move to next question after a short delay
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1000);
                    nextQuestion();
                } catch (InterruptedException e) {
                    nextQuestion();
                }
            });
        }
    }
    
    /**
     * Move to next question
     */
    private void nextQuestion() {
        currentIndex++;
        currentQuestionIndex.setValue(currentIndex);
        
        if (currentIndex < quizQuestions.size()) {
            loadCurrentQuestion();
        } else {
            completeQuiz();
        }
    }
    
    /**
     * Complete the quiz
     */
    private void completeQuiz() {
        isQuizActive.setValue(false);
        isQuizCompleted.setValue(true);
        
        // Save quiz results
        saveQuizResults();
    }
    
    /**
     * Save quiz results to database
     */
    private void saveQuizResults() {
        CompletableFuture.runAsync(() -> {
            try {
                // Create usage event for quiz completion
                UsageEvent event = new UsageEvent();
                event.setEventType("quiz_completed");
                event.setAppName("Smart App Gatekeeper");
                event.setTimestamp(System.currentTimeMillis());
                event.setDetails("Score: " + currentScore + "/" + (quizQuestions.size() * 10));
                
                repository.insertUsageEvent(event);
                
                // Award coins based on score
                int coinsEarned = currentScore / 10; // 1 coin per 10 points
                if (coinsEarned > 0) {
                    // Update user coins (simplified - in real app, update user profile)
                    // repository.addCoins(coinsEarned);
                }
                
            } catch (Exception e) {
                errorMessage.postValue("Failed to save quiz results: " + e.getMessage());
            }
        });
    }
    
    /**
     * Skip current question
     */
    public void skipQuestion() {
        if (isQuizActive.getValue() != null && isQuizActive.getValue()) {
            nextQuestion();
        }
    }
    
    /**
     * Pause quiz
     */
    public void pauseQuiz() {
        isQuizActive.setValue(false);
    }
    
    /**
     * Resume quiz
     */
    public void resumeQuiz() {
        isQuizActive.setValue(true);
    }
    
    /**
     * Restart quiz
     */
    public void restartQuiz() {
        startQuiz();
    }
    
    /**
     * Get quiz progress percentage
     */
    public int getProgressPercentage() {
        if (quizQuestions != null && !quizQuestions.isEmpty()) {
            return (currentIndex * 100) / quizQuestions.size();
        }
        return 0;
    }
    
    /**
     * Get remaining questions count
     */
    public int getRemainingQuestions() {
        if (quizQuestions != null) {
            return Math.max(0, quizQuestions.size() - currentIndex);
        }
        return 0;
    }
    
    // Getters
    public LiveData<List<Question>> getQuestions() {
        return questions;
    }
    
    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }
    
    public LiveData<Integer> getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    public LiveData<Integer> getScore() {
        return score;
    }
    
    public LiveData<Integer> getTimeRemaining() {
        return timeRemaining;
    }
    
    public LiveData<Boolean> getIsQuizActive() {
        return isQuizActive;
    }
    
    public LiveData<Boolean> getIsQuizCompleted() {
        return isQuizCompleted;
    }
    
    public LiveData<String> getSelectedAnswer() {
        return selectedAnswer;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.smartappgatekeeper.model.Question;
import com.smartappgatekeeper.database.entities.QuizResult;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.service.QuestionBankService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Quiz ViewModel - Manages quiz logic and data
 */
public class QuizViewModel extends AndroidViewModel {
    
    // private QuestionRepository questionRepository; // Simplified for now
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();
    private MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<Integer> quizScore = new MutableLiveData<>();
    private MutableLiveData<Integer> correctAnswers = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Integer> currentQuestionIndex = new MutableLiveData<>();
    private MutableLiveData<Integer> score = new MutableLiveData<>();
    private MutableLiveData<Long> timeRemaining = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQuizActive = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQuizCompleted = new MutableLiveData<>();
    private MutableLiveData<Integer> totalQuestions = new MutableLiveData<>();
    
    private List<Question> allQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private int currentScore = 0;
    private int correctCount = 0;
    private boolean quizActive = false;
    private boolean quizCompleted = false;
    
    public QuizViewModel(Application application) {
        super(application);
        // questionRepository = new QuestionRepository(application); // Simplified for now
        quizScore.setValue(0);
        correctAnswers.setValue(0);
        currentQuestionIndex.setValue(0);
        score.setValue(0);
        timeRemaining.setValue(300L); // 5 minutes default
        isQuizActive.setValue(false);
        isQuizCompleted.setValue(false);
        totalQuestions.setValue(0);
    }
    
    /**
     * Load questions for quiz
     */
    public void loadQuestions() {
        // Generate sample questions for now
        generateSampleQuestions();
    }
    
    /**
     * Get next question
     */
    public void nextQuestion() {
        if (currentIndex < allQuestions.size() - 1) {
            currentIndex++;
            currentQuestionIndex.setValue(currentIndex);
            currentQuestion.setValue(allQuestions.get(currentIndex));
        }
    }
    
    /**
     * Get previous question
     */
    public void previousQuestion() {
        if (currentIndex > 0) {
            currentIndex--;
            currentQuestionIndex.setValue(currentIndex);
            currentQuestion.setValue(allQuestions.get(currentIndex));
        }
    }
    
    /**
     * Submit answer
     */
    public void submitAnswer(String selectedAnswer) {
        if (currentIndex < allQuestions.size()) {
            Question question = allQuestions.get(currentIndex);
            boolean isCorrect = selectedAnswer.equals(question.getCorrectAnswer());
            
            if (isCorrect) {
                currentScore += 10; // 10 points per correct answer
                correctCount++;
            }
            
            score.setValue(currentScore);
            quizScore.setValue(currentScore);
            correctAnswers.setValue(correctCount);
        }
    }
    
    /**
     * Save quiz results
     */
    public void saveQuizResults(int finalScore, int correctAnswers, int totalQuestions, double accuracy) {
        // Create QuizResult entity
        QuizResult quizResult = new QuizResult();
        quizResult.quizId = "quiz_" + System.currentTimeMillis();
        quizResult.userId = "user_001"; // TODO: Get actual user ID
        quizResult.totalQuestions = totalQuestions;
        quizResult.correctAnswers = correctAnswers;
        quizResult.wrongAnswers = totalQuestions - correctAnswers;
        quizResult.accuracy = accuracy;
        quizResult.score = finalScore;
        quizResult.timeSpent = 300000 - (timeRemaining.getValue() != null ? timeRemaining.getValue() : 0); // 5 minutes - remaining time
        quizResult.difficulty = getCurrentDifficulty();
        quizResult.topic = getCurrentTopic();
        quizResult.quizType = "practice";
        quizResult.status = "completed";
        quizResult.passingScore = 70; // 70% passing threshold
        
        // Calculate additional metrics
        quizResult.calculateMetrics();
        
        // Calculate rewards
        quizResult.coinsEarned = calculateCoinsEarned(accuracy, totalQuestions);
        quizResult.experiencePoints = calculateExperiencePoints(accuracy, totalQuestions);
        quizResult.isPersonalBest = checkIfPersonalBest(accuracy);
        
        // Set device info
        quizResult.deviceInfo = android.os.Build.MODEL;
        quizResult.appVersion = "1.0.0";
        
        // Save to database
        AppRepository repository = AppRepository.getInstance(getApplication());
        repository.insertQuizResult(quizResult).thenAccept(resultId -> {
            // Update UI with success
            android.util.Log.d("QuizViewModel", "Quiz result saved with ID: " + resultId);
        }).exceptionally(throwable -> {
            android.util.Log.e("QuizViewModel", "Error saving quiz result", throwable);
            return null;
        });
    }
    
    private String getCurrentDifficulty() {
        // Get difficulty from current question or default
        if (currentIndex < allQuestions.size()) {
            return allQuestions.get(currentIndex).getDifficulty();
        }
        return "medium";
    }
    
    private String getCurrentTopic() {
        // Get topic from current question or default
        if (currentIndex < allQuestions.size()) {
            return allQuestions.get(currentIndex).getTopic();
        }
        return "General";
    }
    
    private int calculateCoinsEarned(double accuracy, int totalQuestions) {
        // Only reward coins for correct answers
        int correctAnswers = (int) Math.round((accuracy / 100.0) * totalQuestions);
        
        // Base coins: 10 per correct answer only
        int baseCoins = correctAnswers * 10;
        
        // Bonus for high accuracy (80%+ gets 1.5x multiplier)
        if (accuracy >= 80) {
            baseCoins = (int) (baseCoins * 1.5);
        }
        
        // Minimum 5 coins for attempting quiz
        return Math.max(baseCoins, 5);
    }
    
    private int calculateExperiencePoints(double accuracy, int totalQuestions) {
        // Base XP: 5 per question
        int baseXP = totalQuestions * 5;
        
        // Accuracy bonus
        double accuracyMultiplier = accuracy / 100.0;
        
        return (int) (baseXP * accuracyMultiplier);
    }
    
    private boolean checkIfPersonalBest(double accuracy) {
        // TODO: Check against user's best accuracy from database
        // For now, consider 95%+ as personal best
        return accuracy >= 95.0;
    }
    
    /**
     * Generate questions from comprehensive question bank
     */
    public void generateSampleQuestions() {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        allQuestions = questionBank.getRandomQuestions(15); // Get 15 random questions
        Collections.shuffle(allQuestions);
        
        // Update UI
        if (!allQuestions.isEmpty()) {
            currentQuestion.setValue(allQuestions.get(0));
            totalQuestions.setValue(allQuestions.size());
        }
    }
    
    /**
     * Generate questions by topic
     */
    public void generateQuestionsByTopic(String topic) {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        allQuestions = questionBank.getQuestionsByTopic(topic);
        Collections.shuffle(allQuestions);
        
        if (!allQuestions.isEmpty()) {
            currentQuestion.setValue(allQuestions.get(0));
            totalQuestions.setValue(allQuestions.size());
        }
    }
    
    /**
     * Generate questions by difficulty
     */
    public void generateQuestionsByDifficulty(String difficulty) {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        allQuestions = questionBank.getQuestionsByDifficulty(difficulty);
        Collections.shuffle(allQuestions);
        
        if (!allQuestions.isEmpty()) {
            currentQuestion.setValue(allQuestions.get(0));
            totalQuestions.setValue(allQuestions.size());
        }
    }
    
    /**
     * Get available topics
     */
    public List<String> getAvailableTopics() {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        return questionBank.getAvailableTopics();
    }
    
    /**
     * Get question count by topic
     */
    public int getQuestionCountByTopic(String topic) {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        return questionBank.getQuestionCountByTopic(topic);
    }
    
    /**
     * Get total question count
     */
    public int getTotalQuestionCount() {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        return questionBank.getTotalQuestionCount();
    }
    
    // Getters
    public LiveData<List<Question>> getQuestions() {
        return questions;
    }
    
    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }
    
    public LiveData<Integer> getQuizScore() {
        return quizScore;
    }
    
    public LiveData<Integer> getCorrectAnswers() {
        return correctAnswers;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Integer> getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    public LiveData<Integer> getScore() {
        return score;
    }
    
    public LiveData<Integer> getTotalQuestions() {
        return totalQuestions;
    }
    
    public LiveData<Long> getTimeRemaining() {
        return timeRemaining;
    }
    
    public LiveData<Boolean> getIsQuizActive() {
        return isQuizActive;
    }
    
    public LiveData<Boolean> getIsQuizCompleted() {
        return isQuizCompleted;
    }
    
    
    public int getCorrectCount() {
        return correctCount;
    }
    
    public int getProgressPercentage() {
        if (allQuestions.isEmpty()) return 0;
        return (currentIndex + 1) * 100 / allQuestions.size();
    }
    
    public void startQuiz() {
        quizActive = true;
        isQuizActive.setValue(true);
        loadQuestions();
        startQuizTimer();
    }
    
    /**
     * Start real-time quiz timer
     */
    private void startQuizTimer() {
        android.os.Handler handler = new android.os.Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (quizActive && !quizCompleted) {
                    Long currentTime = timeRemaining.getValue();
                    if (currentTime != null && currentTime > 0) {
                        timeRemaining.setValue(currentTime - 1);
                        handler.postDelayed(this, 1000); // Update every second
                    } else {
                        // Time's up!
                        completeQuiz();
                    }
                }
            }
        };
        handler.post(timerRunnable);
    }
    
    public void skipQuestion() {
        nextQuestion();
    }
    
    public void pauseQuiz() {
        quizActive = false;
        isQuizActive.setValue(false);
    }
    
    public void resumeQuiz() {
        quizActive = true;
        isQuizActive.setValue(true);
    }
    
    public void completeQuiz() {
        quizCompleted = true;
        isQuizCompleted.setValue(true);
        quizActive = false;
        isQuizActive.setValue(false);
    }
}
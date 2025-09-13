package com.vijayapardhu.quizlock.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vijayapardhu.quizlock.db.QuizHistory;
import com.vijayapardhu.quizlock.db.QuizHistoryDao;
import com.vijayapardhu.quizlock.db.QuizLockDatabase;
import com.vijayapardhu.quizlock.db.QuizQuestion;
import com.vijayapardhu.quizlock.db.QuizQuestionDao;
import com.vijayapardhu.quizlock.db.UserPreferencesDao;
import com.vijayapardhu.quizlock.network.GeminiApiService;
import com.vijayapardhu.quizlock.network.GeminiRequest;
import com.vijayapardhu.quizlock.network.GeminiResponse;
import com.vijayapardhu.quizlock.network.NetworkClient;
import com.vijayapardhu.quizlock.utils.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewModel extends AndroidViewModel {
    
    private static final String TAG = "QuizViewModel";
    
    private final QuizQuestionDao quizQuestionDao;
    private final QuizHistoryDao quizHistoryDao;
    private final UserPreferencesDao userPreferencesDao;
    private final GeminiApiService geminiApiService;
    private final ExecutorService executor;
    
    private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answerCorrect = new MutableLiveData<>();
    private final MutableLiveData<Integer> cooldownSeconds = new MutableLiveData<>(0);
    
    private String targetPackageName;
    private long questionStartTime;
    private int consecutiveWrongAnswers = 0;
    
    public QuizViewModel(@NonNull Application application) {
        super(application);
        QuizLockDatabase database = QuizLockDatabase.getDatabase(application);
        quizQuestionDao = database.quizQuestionDao();
        quizHistoryDao = database.quizHistoryDao();
        userPreferencesDao = database.userPreferencesDao();
        geminiApiService = NetworkClient.getGeminiApiService();
        executor = Executors.newFixedThreadPool(2);
    }
    
    public void loadQuestionForApp(String packageName) {
        this.targetPackageName = packageName;
        this.questionStartTime = System.currentTimeMillis();
        
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                // Get user's selected topics
                String selectedTopicsStr = userPreferencesDao.getPreference(Constants.PREF_SELECTED_TOPICS);
                List<String> selectedTopics = selectedTopicsStr != null ? 
                    Arrays.asList(selectedTopicsStr.split(",")) : 
                    Arrays.asList(Constants.TOPIC_MATH, Constants.TOPIC_GENERAL_KNOWLEDGE);
                
                // Try to get question from API first, fallback to offline
                String randomTopic = selectedTopics.get(new Random().nextInt(selectedTopics.size()));
                String difficulty = getDifficultyBasedOnWrongAnswers();
                
                loadQuestionFromAPI(randomTopic, difficulty, () -> {
                    // Fallback to offline question
                    loadOfflineQuestion(randomTopic);
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Error loading question", e);
                loadOfflineQuestion(Constants.TOPIC_MATH); // Ultimate fallback
            }
        });
    }
    
    private void loadQuestionFromAPI(String topic, String difficulty, Runnable fallback) {
        String apiKey = userPreferencesDao.getPreference(Constants.PREF_GEMINI_API_KEY);
        if (apiKey == null || apiKey.isEmpty()) {
            fallback.run();
            return;
        }
        
        String prompt = buildQuizPrompt(topic, difficulty);
        GeminiRequest request = new GeminiRequest(prompt);
        
        Call<GeminiResponse> call = geminiApiService.generateContent(apiKey, request);
        call.enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String generatedText = response.body().getGeneratedText();
                    if (generatedText != null) {
                        QuizQuestion question = parseQuestionFromAPI(generatedText, topic, difficulty);
                        if (question != null) {
                            executor.execute(() -> {
                                quizQuestionDao.insertQuestion(question);
                                currentQuestion.postValue(question);
                                isLoading.postValue(false);
                            });
                            return;
                        }
                    }
                }
                // Fallback to offline
                fallback.run();
            }
            
            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                fallback.run();
            }
        });
    }
    
    private void loadOfflineQuestion(String topic) {
        executor.execute(() -> {
            QuizQuestion question = quizQuestionDao.getRandomOfflineQuestionByTopic(topic);
            if (question == null) {
                // If no questions for this topic, get any offline question
                question = quizQuestionDao.getRandomOfflineQuestionByTopic(Constants.TOPIC_MATH);
            }
            
            if (question != null) {
                currentQuestion.postValue(question);
            } else {
                errorMessage.postValue("No questions available");
            }
            isLoading.postValue(false);
        });
    }
    
    private String buildQuizPrompt(String topic, String difficulty) {
        return String.format(
            "Generate a %s difficulty multiple choice question about %s. " +
            "Format your response exactly as follows:\n" +
            "Question: [Your question here]\n" +
            "A) [Option A]\n" +
            "B) [Option B]\n" +
            "C) [Option C]\n" +
            "D) [Option D]\n" +
            "Answer: [A, B, C, or D]\n" +
            "Keep it concise and educational.",
            difficulty, topic
        );
    }
    
    private QuizQuestion parseQuestionFromAPI(String generatedText, String topic, String difficulty) {
        try {
            String[] lines = generatedText.split("\n");
            String question = "";
            String optionA = "", optionB = "", optionC = "", optionD = "";
            String answer = "";
            
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("Question:")) {
                    question = line.substring(9).trim();
                } else if (line.startsWith("A)")) {
                    optionA = line.substring(2).trim();
                } else if (line.startsWith("B)")) {
                    optionB = line.substring(2).trim();
                } else if (line.startsWith("C)")) {
                    optionC = line.substring(2).trim();
                } else if (line.startsWith("D)")) {
                    optionD = line.substring(2).trim();
                } else if (line.startsWith("Answer:")) {
                    answer = line.substring(7).trim();
                }
            }
            
            if (!question.isEmpty() && !optionA.isEmpty() && !answer.isEmpty()) {
                return new QuizQuestion(question, optionA, optionB, optionC, optionD, 
                                      answer, topic, difficulty, true);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing API response", e);
        }
        return null;
    }
    
    public void submitAnswer(String selectedAnswer) {
        QuizQuestion question = currentQuestion.getValue();
        if (question == null) return;
        
        boolean isCorrect = selectedAnswer.equals(question.getCorrectAnswer());
        answerCorrect.setValue(isCorrect);
        
        // Calculate time taken
        int timeTaken = (int) ((System.currentTimeMillis() - questionStartTime) / 1000);
        
        // Save to history
        executor.execute(() -> {
            QuizHistory history = new QuizHistory(
                targetPackageName, 
                question.getQuestion(),
                selectedAnswer,
                question.getCorrectAnswer(),
                isCorrect,
                question.getTopic(),
                timeTaken
            );
            quizHistoryDao.insertHistory(history);
            
            if (isCorrect) {
                updateStreak();
                consecutiveWrongAnswers = 0;
            } else {
                consecutiveWrongAnswers++;
                if (consecutiveWrongAnswers >= 2) {
                    // Start cooldown
                    cooldownSeconds.postValue(Constants.COOLDOWN_MINUTES * 60);
                }
            }
        });
    }
    
    private void updateStreak() {
        executor.execute(() -> {
            String currentStreakStr = userPreferencesDao.getPreference(Constants.PREF_CURRENT_STREAK);
            String bestStreakStr = userPreferencesDao.getPreference(Constants.PREF_BEST_STREAK);
            
            int currentStreak = currentStreakStr != null ? Integer.parseInt(currentStreakStr) : 0;
            int bestStreak = bestStreakStr != null ? Integer.parseInt(bestStreakStr) : 0;
            
            currentStreak++;
            if (currentStreak > bestStreak) {
                bestStreak = currentStreak;
                userPreferencesDao.insertPreference(
                    new UserPreferences(Constants.PREF_BEST_STREAK, String.valueOf(bestStreak))
                );
            }
            
            userPreferencesDao.insertPreference(
                new UserPreferences(Constants.PREF_CURRENT_STREAK, String.valueOf(currentStreak))
            );
        });
    }
    
    private String getDifficultyBasedOnWrongAnswers() {
        if (consecutiveWrongAnswers >= 3) {
            return Constants.DIFFICULTY_EASY;
        } else if (consecutiveWrongAnswers >= 1) {
            return Constants.DIFFICULTY_MEDIUM;
        } else {
            return Constants.DIFFICULTY_HARD;
        }
    }
    
    public void emergencyUnlock() {
        // Log emergency unlock
        executor.execute(() -> {
            QuizHistory history = new QuizHistory(
                targetPackageName,
                "Emergency Unlock",
                "EMERGENCY",
                "QUIZ",
                false,
                "Emergency",
                0
            );
            quizHistoryDao.insertHistory(history);
        });
    }
    
    // Getters
    public LiveData<QuizQuestion> getCurrentQuestion() { return currentQuestion; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getAnswerCorrect() { return answerCorrect; }
    public LiveData<Integer> getCooldownSeconds() { return cooldownSeconds; }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
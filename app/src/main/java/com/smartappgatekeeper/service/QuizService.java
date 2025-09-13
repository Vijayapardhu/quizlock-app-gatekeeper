package com.smartappgatekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.Question;
import com.smartappgatekeeper.database.entities.UsageEvent;
import com.smartappgatekeeper.service.AIQuestionService;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Service for managing quiz operations
 * Handles question generation, scoring, and quiz session management
 */
public class QuizService extends Service {
    
    private static final String TAG = "QuizService";
    private AppRepository repository;
    private AIQuestionService aiQuestionService;
    private Random random = new Random();
    
    @Override
    public void onCreate() {
        super.onCreate();
        repository = AppRepository.getInstance(getApplication());
        aiQuestionService = AIQuestionService.getInstance(getApplication());
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "GENERATE_QUIZ":
                        int difficulty = intent.getIntExtra("difficulty", 1);
                        int questionCount = intent.getIntExtra("question_count", 10);
                        generateQuiz(difficulty, questionCount);
                        break;
                    case "SUBMIT_ANSWER":
                        String questionId = intent.getStringExtra("question_id");
                        String answer = intent.getStringExtra("answer");
                        submitAnswer(questionId, answer);
                        break;
                    case "COMPLETE_QUIZ":
                        int score = intent.getIntExtra("score", 0);
                        int totalQuestions = intent.getIntExtra("total_questions", 0);
                        completeQuiz(score, totalQuestions);
                        break;
                    case "GET_RANDOM_QUESTION":
                        getRandomQuestion();
                        break;
                }
            }
        }
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Generate a quiz with specified parameters
     */
    private void generateQuiz(int difficulty, int questionCount) {
        CompletableFuture.runAsync(() -> {
            try {
                repository.getAllQuestions().observeForever(questions -> {
                    if (questions != null && !questions.isEmpty()) {
                        List<Question> quizQuestions = selectQuestionsForQuiz(questions, difficulty, questionCount);
                        
                        // Log quiz generation
                        Log.i(TAG, "Generated quiz with " + quizQuestions.size() + " questions (difficulty: " + difficulty + ")");
                        
                        // Track quiz start event
                        trackEvent("quiz_started", "Smart App Gatekeeper", 
                                 "Questions: " + quizQuestions.size() + ", Difficulty: " + difficulty);
                        
                    } else {
                        Log.w(TAG, "No questions available for quiz generation, generating AI questions...");
                        generateAIQuestions(difficulty, questionCount);
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to generate quiz: " + e.getMessage());
            }
        });
    }
    
    /**
     * Generate questions using AI when database is empty
     */
    private void generateAIQuestions(int difficulty, int questionCount) {
        if (aiQuestionService != null && aiQuestionService.isAvailable()) {
            String[] topics = aiQuestionService.getAvailableTopics();
            String difficultyLevel = getDifficultyString(difficulty);
            
            // Generate questions for multiple topics
            aiQuestionService.generateQuestionsForTopics(topics, difficultyLevel, questionCount / topics.length + 1)
                    .thenAccept(success -> {
                        if (success) {
                            Log.i(TAG, "Successfully generated AI questions");
                            // Retry quiz generation after AI questions are created
                            generateQuiz(difficulty, questionCount);
                        } else {
                            Log.e(TAG, "Failed to generate AI questions");
                        }
                    });
        } else {
            Log.w(TAG, "AI question service not available, using mock questions");
            createMockQuestions();
        }
    }
    
    /**
     * Convert difficulty integer to string
     */
    private String getDifficultyString(int difficulty) {
        switch (difficulty) {
            case 1: return "easy";
            case 2: return "medium";
            case 3: return "hard";
            default: return "medium";
        }
    }
    
    /**
     * Create mock questions when AI is not available
     */
    private void createMockQuestions() {
        List<Question> mockQuestions = new ArrayList<>();
        
        Question question1 = new Question();
        question1.setQuestionText("What is the primary purpose of this app?");
        question1.setOptionA("To help reduce screen time through gamified learning");
        question1.setOptionB("To increase social media usage");
        question1.setOptionC("To play games");
        question1.setOptionD("To waste time");
        question1.setCorrectAnswer("A");
        question1.setExplanation("This app helps users control their screen time by requiring them to answer educational questions before accessing restricted apps.");
        mockQuestions.add(question1);
        
        Question question2 = new Question();
        question2.setQuestionText("Which programming language is known for its simplicity and readability?");
        question2.setOptionA("Python");
        question2.setOptionB("Assembly");
        question2.setOptionC("Binary");
        question2.setOptionD("Machine Code");
        question2.setCorrectAnswer("A");
        question2.setExplanation("Python is widely known for its simple, readable syntax that makes it easy for beginners to learn programming.");
        mockQuestions.add(question2);
        
        Question question3 = new Question();
        question3.setQuestionText("What does CPU stand for?");
        question3.setOptionA("Central Processing Unit");
        question3.setOptionB("Computer Personal Unit");
        question3.setOptionC("Central Program Unit");
        question3.setOptionD("Computer Processing Unit");
        question3.setCorrectAnswer("A");
        question3.setExplanation("CPU stands for Central Processing Unit, which is the brain of the computer that performs most calculations.");
        mockQuestions.add(question3);
        
        // Save mock questions to database
        for (Question question : mockQuestions) {
            try {
                repository.insertQuestion(question);
            } catch (Exception e) {
                Log.e(TAG, "Failed to insert mock question: " + e.getMessage());
            }
        }
        
        Log.i(TAG, "Created " + mockQuestions.size() + " mock questions");
    }
    
    /**
     * Select questions for quiz based on difficulty and count
     */
    private List<Question> selectQuestionsForQuiz(List<Question> allQuestions, int difficulty, int questionCount) {
        List<Question> filteredQuestions = new ArrayList<>();
        
        // Filter questions by difficulty (simplified - in real app, questions would have difficulty levels)
        for (Question question : allQuestions) {
            if (isQuestionSuitableForDifficulty(question, difficulty)) {
                filteredQuestions.add(question);
            }
        }
        
        // If not enough questions, use all available
        if (filteredQuestions.size() < questionCount) {
            filteredQuestions = new ArrayList<>(allQuestions);
        }
        
        // Shuffle and select required count
        Collections.shuffle(filteredQuestions);
        return filteredQuestions.size() > questionCount ? 
            filteredQuestions.subList(0, questionCount) : filteredQuestions;
    }
    
    /**
     * Check if question is suitable for difficulty level
     */
    private boolean isQuestionSuitableForDifficulty(Question question, int difficulty) {
        // Simplified difficulty check
        // In a real app, questions would have difficulty levels stored in database
        switch (difficulty) {
            case 1: // Easy
                return question.getQuestion().length() < 100; // Shorter questions are easier
            case 2: // Medium
                return question.getQuestion().length() >= 100 && question.getQuestion().length() < 200;
            case 3: // Hard
                return question.getQuestion().length() >= 200;
            default:
                return true;
        }
    }
    
    /**
     * Submit an answer for a question
     */
    private void submitAnswer(String questionId, String answer) {
        CompletableFuture.runAsync(() -> {
            try {
                repository.getAllQuestions().observeForever(questions -> {
                    if (questions != null) {
                        for (Question question : questions) {
                            if (String.valueOf(question.getId()).equals(questionId)) {
                                boolean isCorrect = question.getCorrectAnswer().equals(answer);
                                
                                // Track answer submission
                                trackEvent("answer_submitted", "Smart App Gatekeeper", 
                                         "Question: " + questionId + ", Correct: " + isCorrect);
                                
                                if (isCorrect) {
                                    // Award points for correct answer
                                    trackEvent("correct_answer", "Smart App Gatekeeper", 
                                             "Question: " + questionId);
                                } else {
                                    // Track incorrect answer
                                    trackEvent("incorrect_answer", "Smart App Gatekeeper", 
                                             "Question: " + questionId + ", Answer: " + answer);
                                }
                                
                                break;
                            }
                        }
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to submit answer: " + e.getMessage());
            }
        });
    }
    
    /**
     * Complete quiz and calculate results
     */
    private void completeQuiz(int score, int totalQuestions) {
        CompletableFuture.runAsync(() -> {
            try {
                double accuracy = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
                int timeSaved = score * 2; // 2 minutes per correct answer
                
                // Track quiz completion
                trackEvent("quiz_completed", "Smart App Gatekeeper", 
                         "Score: " + score + "/" + totalQuestions + 
                         ", Accuracy: " + String.format("%.1f", accuracy) + "%" +
                         ", Time Saved: " + timeSaved + " minutes");
                
                // Award coins based on performance
                int coinsEarned = calculateCoinsEarned(score, totalQuestions, accuracy);
                if (coinsEarned > 0) {
                    trackEvent("coins_earned", "Smart App Gatekeeper", 
                             "Amount: " + coinsEarned + ", Source: Quiz");
                }
                
                // Check for achievements
                checkQuizAchievements(score, totalQuestions, accuracy);
                
                Log.i(TAG, "Quiz completed - Score: " + score + "/" + totalQuestions + 
                      ", Accuracy: " + String.format("%.1f", accuracy) + "%" +
                      ", Coins: " + coinsEarned);
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to complete quiz: " + e.getMessage());
            }
        });
    }
    
    /**
     * Get a random question
     */
    private void getRandomQuestion() {
        CompletableFuture.runAsync(() -> {
            try {
                repository.getAllQuestions().observeForever(questions -> {
                    if (questions != null && !questions.isEmpty()) {
                        Question randomQuestion = questions.get(random.nextInt(questions.size()));
                        
                        // Track question request
                        trackEvent("question_requested", "Smart App Gatekeeper", 
                                 "Question ID: " + randomQuestion.getId());
                        
                        Log.d(TAG, "Random question selected: " + randomQuestion.getQuestion());
                    } else {
                        Log.w(TAG, "No questions available for random selection");
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to get random question: " + e.getMessage());
            }
        });
    }
    
    /**
     * Calculate coins earned based on quiz performance
     */
    private int calculateCoinsEarned(int score, int totalQuestions, double accuracy) {
        int baseCoins = score; // 1 coin per correct answer
        int bonusCoins = 0;
        
        // Accuracy bonus
        if (accuracy >= 90) {
            bonusCoins += 5; // Perfect accuracy bonus
        } else if (accuracy >= 80) {
            bonusCoins += 3; // High accuracy bonus
        } else if (accuracy >= 70) {
            bonusCoins += 1; // Good accuracy bonus
        }
        
        // Streak bonus (simplified)
        if (score == totalQuestions) {
            bonusCoins += 2; // Perfect score bonus
        }
        
        return baseCoins + bonusCoins;
    }
    
    /**
     * Check for quiz-related achievements
     */
    private void checkQuizAchievements(int score, int totalQuestions, double accuracy) {
        // Perfect score achievement
        if (score == totalQuestions) {
            trackEvent("achievement_unlocked", "Smart App Gatekeeper", 
                     "Perfect Score: " + score + "/" + totalQuestions);
        }
        
        // High accuracy achievement
        if (accuracy >= 90) {
            trackEvent("achievement_unlocked", "Smart App Gatekeeper", 
                     "High Accuracy: " + String.format("%.1f", accuracy) + "%");
        }
        
        // Streak achievements (simplified)
        if (score >= 5) {
            trackEvent("achievement_unlocked", "Smart App Gatekeeper", 
                     "Good Performance: " + score + " correct answers");
        }
    }
    
    /**
     * Track an event
     */
    private void trackEvent(String eventType, String appName, String details) {
        try {
            UsageEvent event = new UsageEvent();
            event.setEventType(eventType);
            event.setAppName(appName);
            event.setTimestamp(System.currentTimeMillis());
            event.setDetails(details);
            
            repository.insertUsageEvent(event);
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to track event: " + e.getMessage());
        }
    }
}

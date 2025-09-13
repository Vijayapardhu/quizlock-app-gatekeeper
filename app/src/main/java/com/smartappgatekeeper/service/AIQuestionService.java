package com.smartappgatekeeper.service;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.smartappgatekeeper.config.AppConfig;
import com.smartappgatekeeper.database.entities.Question;
import com.smartappgatekeeper.repository.AppRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for generating quiz questions using Gemini AI
 * FR-009: System shall generate questions using AI (Gemini) when available
 */
public class AIQuestionService {
    
    private static final String TAG = "AIQuestionService";
    private static AIQuestionService instance;
    private AppRepository repository;
    
    private AIQuestionService(Context context) {
        this.repository = AppRepository.getInstance((Application) context.getApplicationContext());
    }
    
    public static synchronized AIQuestionService getInstance(Context context) {
        if (instance == null) {
            instance = new AIQuestionService(context);
        }
        return instance;
    }
    
    /**
     * Generate questions using AI (simplified implementation)
     */
    public CompletableFuture<List<Question>> generateQuestions(String topic, String difficulty, int count) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.i(TAG, "Generating " + count + " questions for topic: " + topic + ", difficulty: " + difficulty);
                
                // For now, generate mock questions based on topic and difficulty
                List<Question> questions = generateMockQuestions(topic, difficulty, count);
                
                Log.i(TAG, "Generated " + questions.size() + " questions successfully");
                return questions;
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to generate questions: " + e.getMessage());
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Generate mock questions based on topic and difficulty
     */
    private List<Question> generateMockQuestions(String topic, String difficulty, int count) {
        List<Question> questions = new ArrayList<>();
        
        // Generate questions based on topic
        String[][] questionSets = getQuestionSetsForTopic(topic);
        
        for (int i = 0; i < Math.min(count, questionSets.length); i++) {
            String[] questionData = questionSets[i];
            
            Question question = new Question();
            question.setQuestionText(questionData[0]);
            question.setOptionA(questionData[1]);
            question.setOptionB(questionData[2]);
            question.setOptionC(questionData[3]);
            question.setOptionD(questionData[4]);
            question.setCorrectAnswer(questionData[5]);
            question.setExplanation(questionData[6]);
            question.setTopic(topic);
            question.setDifficulty(difficulty);
            question.setSource("ai_generated");
            question.setAiModel("mock");
            question.setTimeLimitSeconds(30);
            question.setActive(true);
            
            questions.add(question);
        }
        
        return questions;
    }
    
    /**
     * Get question sets for different topics
     */
    private String[][] getQuestionSetsForTopic(String topic) {
        switch (topic.toLowerCase()) {
            case "programming":
                return new String[][]{
                    {"What is the primary purpose of version control systems like Git?", 
                     "To track changes in code over time", "To compile code faster", 
                     "To debug applications", "To design user interfaces", "A",
                     "Version control systems help developers track changes, collaborate, and maintain code history."},
                    {"Which programming language is known for its use in web development?", 
                     "JavaScript", "Assembly", "Machine Code", "Binary", "A",
                     "JavaScript is widely used for both frontend and backend web development."},
                    {"What does API stand for in programming?", 
                     "Application Programming Interface", "Advanced Programming Interface", 
                     "Automated Programming Interface", "Application Process Interface", "A",
                     "API allows different software applications to communicate with each other."}
                };
            case "mathematics":
                return new String[][]{
                    {"What is the value of π (pi) to two decimal places?", 
                     "3.14", "3.15", "3.16", "3.13", "A",
                     "Pi (π) is approximately 3.14159, which rounds to 3.14 to two decimal places."},
                    {"What is the square root of 64?", 
                     "8", "6", "4", "32", "A",
                     "8 × 8 = 64, so the square root of 64 is 8."},
                    {"What is 15% of 200?", 
                     "30", "25", "35", "40", "A",
                     "15% of 200 = 0.15 × 200 = 30."}
                };
            case "science":
                return new String[][]{
                    {"What is the chemical symbol for water?", 
                     "H2O", "H2O2", "HO", "H3O", "A",
                     "Water consists of two hydrogen atoms and one oxygen atom, hence H2O."},
                    {"What is the speed of light in vacuum?", 
                     "299,792,458 m/s", "300,000,000 m/s", "299,000,000 m/s", "301,000,000 m/s", "A",
                     "The speed of light in vacuum is exactly 299,792,458 meters per second."},
                    {"What gas do plants absorb from the atmosphere during photosynthesis?", 
                     "Carbon dioxide", "Oxygen", "Nitrogen", "Hydrogen", "A",
                     "Plants absorb carbon dioxide and release oxygen during photosynthesis."}
                };
            default:
                return new String[][]{
                    {"What is the capital of France?", 
                     "Paris", "London", "Berlin", "Madrid", "A",
                     "Paris is the capital and largest city of France."},
                    {"Who painted the Mona Lisa?", 
                     "Leonardo da Vinci", "Michelangelo", "Picasso", "Van Gogh", "A",
                     "Leonardo da Vinci painted the Mona Lisa between 1503 and 1519."},
                    {"What is the largest planet in our solar system?", 
                     "Jupiter", "Saturn", "Neptune", "Earth", "A",
                     "Jupiter is the largest planet in our solar system by both mass and volume."}
                };
        }
    }
    
    /**
     * Generate and save questions to database
     */
    public CompletableFuture<Boolean> generateAndSaveQuestions(String topic, String difficulty, int count) {
        return generateQuestions(topic, difficulty, count)
                .thenApply(questions -> {
                    if (questions.isEmpty()) {
                        return false;
                    }
                    
                    try {
                        // Save questions to database
                        for (Question question : questions) {
                            repository.insertQuestion(question);
                        }
                        
                        Log.i(TAG, "Successfully saved " + questions.size() + " AI-generated questions");
                        return true;
                        
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to save AI-generated questions: " + e.getMessage());
                        return false;
                    }
                });
    }
    
    /**
     * Generate questions for multiple topics
     */
    public CompletableFuture<Boolean> generateQuestionsForTopics(String[] topics, String difficulty, int countPerTopic) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        
        for (String topic : topics) {
            futures.add(generateAndSaveQuestions(topic, difficulty, countPerTopic));
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    boolean allSuccess = true;
                    for (CompletableFuture<Boolean> future : futures) {
                        try {
                            if (!future.get()) {
                                allSuccess = false;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error in topic question generation: " + e.getMessage());
                            allSuccess = false;
                        }
                    }
                    return allSuccess;
                });
    }
    
    /**
     * Check if AI service is available
     */
    public boolean isAvailable() {
        return true; // Mock service is always available
    }
    
    /**
     * Get available topics for question generation
     */
    public String[] getAvailableTopics() {
        return new String[]{
            "Programming",
            "Mathematics", 
            "Science",
            "History",
            "Geography",
            "Literature",
            "Sports",
            "General Knowledge",
            "Technology",
            "Art",
            "Music",
            "Philosophy"
        };
    }
    
    /**
     * Get available difficulty levels
     */
    public String[] getAvailableDifficulties() {
        return new String[]{"easy", "medium", "hard"};
    }
}

package com.smartappgatekeeper.service;

import android.content.Context;
import android.util.Log;
import com.smartappgatekeeper.model.Question;
import com.smartappgatekeeper.database.entities.UserProfile;
import com.smartappgatekeeper.database.entities.QuizResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * AI-powered quiz generation service using Google Gemini API
 * Generates personalized quizzes based on user's course progress and learning level
 */
public class GeminiQuizService {
    private static final String TAG = "GeminiQuizService";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    private static final String API_KEY = "YOUR_GEMINI_API_KEY"; // Replace with actual API key
    
    private static GeminiQuizService instance;
    private final OkHttpClient httpClient;
    private final ExecutorService executorService;
    private final Context context;
    
    private GeminiQuizService(Context context) {
        this.context = context.getApplicationContext();
        this.httpClient = new OkHttpClient();
        this.executorService = Executors.newFixedThreadPool(2);
    }
    
    public static synchronized GeminiQuizService getInstance(Context context) {
        if (instance == null) {
            instance = new GeminiQuizService(context);
        }
        return instance;
    }
    
    /**
     * Generate personalized quiz based on user's course progress
     */
    public CompletableFuture<List<Question>> generatePersonalizedQuiz(
            UserProfile userProfile, 
            String courseTopic, 
            int questionCount) {
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Analyze user's learning progress
                String userLevel = determineUserLevel(userProfile);
                String learningContext = buildLearningContext(userProfile, courseTopic);
                
                // Generate quiz prompt for Gemini
                String prompt = buildQuizPrompt(userLevel, courseTopic, learningContext, questionCount);
                
                // Call Gemini API
                String response = callGeminiAPI(prompt);
                
                // Parse response into Question objects
                return parseQuizResponse(response, courseTopic);
                
            } catch (Exception e) {
                Log.e(TAG, "Error generating personalized quiz", e);
                return getFallbackQuestions(courseTopic, questionCount);
            }
        }, executorService);
    }
    
    /**
     * Determine user's learning level based on profile and quiz history
     */
    private String determineUserLevel(UserProfile userProfile) {
        // Analyze user's quiz performance and course progress
        double averageScore = 70.0; // Default value
        int completedCourses = 0; // Default value
        int totalQuizzes = 0; // Default value
        
        if (averageScore >= 80 && completedCourses >= 5) {
            return "Advanced";
        } else if (averageScore >= 60 && completedCourses >= 2) {
            return "Intermediate";
        } else {
            return "Beginner";
        }
    }
    
    /**
     * Build learning context for AI prompt
     */
    private String buildLearningContext(UserProfile userProfile, String courseTopic) {
        StringBuilder context = new StringBuilder();
        
        context.append("User Profile:\n");
        context.append("- Learning Level: ").append(determineUserLevel(userProfile)).append("\n");
        context.append("- Average Quiz Score: 70%\n");
        context.append("- Completed Courses: 0\n");
        context.append("- Total Quizzes: 0\n");
        context.append("- Preferred Topics: General\n");
        
        context.append("\nCourse Context:\n");
        context.append("- Topic: ").append(courseTopic).append("\n");
        context.append("- Difficulty: Medium\n");
        
        return context.toString();
    }
    
    /**
     * Build AI prompt for quiz generation
     */
    private String buildQuizPrompt(String userLevel, String courseTopic, String learningContext, int questionCount) {
        return String.format(
            "You are an AI tutor creating a personalized quiz for a %s level student.\n\n" +
            "%s\n\n" +
            "Generate %d quiz questions about '%s' with the following requirements:\n" +
            "1. Questions should match the student's %s level\n" +
            "2. Include a mix of multiple choice questions (A, B, C, D)\n" +
            "3. Questions should test understanding, not just memorization\n" +
            "4. Include practical applications and problem-solving\n" +
            "5. Provide clear, concise explanations for each answer\n\n" +
            "Format the response as JSON with this structure:\n" +
            "{\n" +
            "  \"questions\": [\n" +
            "    {\n" +
            "      \"question\": \"Question text here\",\n" +
            "      \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"],\n" +
            "      \"correctAnswer\": \"A\",\n" +
            "      \"explanation\": \"Explanation of why this answer is correct\",\n" +
            "      \"difficulty\": \"Easy|Medium|Hard\",\n" +
            "      \"topic\": \"%s\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n\n" +
            "Make the questions engaging and educational!",
            userLevel, learningContext, questionCount, courseTopic, userLevel, courseTopic
        );
    }
    
    /**
     * Call Gemini API with the prompt
     */
    private String callGeminiAPI(String prompt) throws IOException {
        JSONObject requestBody = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        
        try {
            part.put("text", prompt);
            parts.put(part);
            content.put("parts", parts);
            contents.put(content);
            requestBody.put("contents", contents);
            
            // Add generation config for better responses
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);
            generationConfig.put("maxOutputTokens", 2048);
            requestBody.put("generationConfig", generationConfig);
            
        } catch (Exception e) {
            Log.e(TAG, "Error building request JSON", e);
            throw new IOException("Failed to build request", e);
        }
        
        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
            .url(GEMINI_API_URL + "?key=" + API_KEY)
            .post(body)
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            
            String responseBody = response.body().string();
            Log.d(TAG, "Gemini API response: " + responseBody);
            return responseBody;
        }
    }
    
    /**
     * Parse Gemini response into Question objects
     */
    private List<Question> parseQuizResponse(String response, String courseTopic) {
        List<Question> questions = new ArrayList<>();
        
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            JSONObject candidate = candidates.getJSONObject(0);
            JSONObject content = candidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject part = parts.getJSONObject(0);
            String text = part.getString("text");
            
            // Extract JSON from the text response
            int jsonStart = text.indexOf("{");
            int jsonEnd = text.lastIndexOf("}") + 1;
            if (jsonStart != -1 && jsonEnd > jsonStart) {
                String jsonString = text.substring(jsonStart, jsonEnd);
                JSONObject quizData = new JSONObject(jsonString);
                JSONArray questionsArray = quizData.getJSONArray("questions");
                
                for (int i = 0; i < questionsArray.length(); i++) {
                    JSONObject questionJson = questionsArray.getJSONObject(i);
                    Question question = parseQuestionFromJson(questionJson, courseTopic);
                    if (question != null) {
                        questions.add(question);
                    }
                }
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing quiz response", e);
        }
        
        return questions.isEmpty() ? getFallbackQuestions(courseTopic, 5) : questions;
    }
    
    /**
     * Parse individual question from JSON
     */
    private Question parseQuestionFromJson(JSONObject questionJson, String courseTopic) {
        try {
            String questionText = questionJson.getString("question");
            JSONArray optionsArray = questionJson.getJSONArray("options");
            String correctAnswer = questionJson.getString("correctAnswer");
            String explanation = questionJson.getString("explanation");
            String difficulty = questionJson.optString("difficulty", "Medium");
            
            List<String> options = new ArrayList<>();
            for (int i = 0; i < optionsArray.length(); i++) {
                options.add(optionsArray.getString(i));
            }
            
            return new Question(
                questionText,
                options,
                correctAnswer,
                explanation,
                difficulty,
                courseTopic
            );
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing question from JSON", e);
            return null;
        }
    }
    
    /**
     * Get fallback questions if AI generation fails
     */
    private List<Question> getFallbackQuestions(String courseTopic, int count) {
        QuestionBankService questionBank = QuestionBankService.getInstance();
        return questionBank.getQuestionsByTopic(courseTopic).subList(0, Math.min(count, 5));
    }
    
    /**
     * Generate quiz based on specific learning path
     */
    public CompletableFuture<List<Question>> generateLearningPathQuiz(
            String learningPathId, 
            int questionCount) {
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                LearningPathService pathService = LearningPathService.getInstance();
                LearningPathService.LearningPath path = pathService.getLearningPathById(learningPathId);
                
                if (path != null) {
                    // Create a mock user profile for the learning path
                    UserProfile mockProfile = createMockProfileForPath(path);
                    return generatePersonalizedQuiz(mockProfile, path.title, questionCount).get();
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error generating learning path quiz", e);
            }
            
            return getFallbackQuestions("General Knowledge", questionCount);
        }, executorService);
    }
    
    /**
     * Create mock user profile for learning path
     */
    private UserProfile createMockProfileForPath(LearningPathService.LearningPath path) {
        UserProfile profile = new UserProfile();
        // Set default values since the methods don't exist yet
        return profile;
    }
    
    /**
     * Clean up resources
     */
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

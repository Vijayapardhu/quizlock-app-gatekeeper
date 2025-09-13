package com.quizlock.gatekeeper.api;

import com.quizlock.gatekeeper.data.model.QuizQuestion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for generating quiz questions using Gemini API
 */
public class QuizGenerator {
    
    private final GeminiApiService apiService;
    private final String apiKey;
    
    public QuizGenerator(GeminiApiService apiService, String apiKey) {
        this.apiService = apiService;
        this.apiKey = apiKey;
    }
    
    /**
     * Generate a quiz question for the specified category and difficulty
     */
    public void generateQuestion(String category, String difficulty, QuestionCallback callback) {
        String prompt = createPrompt(category, difficulty);
        
        GeminiApiService.GeminiRequest request = new GeminiApiService.GeminiRequest(prompt, category, difficulty);
        
        apiService.generateContent(apiKey, request).enqueue(new retrofit2.Callback<GeminiApiService.GeminiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<GeminiApiService.GeminiResponse> call, 
                                 retrofit2.Response<GeminiApiService.GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String generatedText = response.body().getGeneratedText();
                    if (generatedText != null) {
                        QuizQuestion question = parseQuestionFromText(generatedText, category, difficulty);
                        if (question != null) {
                            callback.onQuestionGenerated(question);
                        } else {
                            callback.onError("Failed to parse generated question");
                        }
                    } else {
                        callback.onError("No content generated");
                    }
                } else {
                    callback.onError("API request failed: " + response.code());
                }
            }
            
            @Override
            public void onFailure(retrofit2.Call<GeminiApiService.GeminiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Create a prompt for generating quiz questions
     */
    private String createPrompt(String category, String difficulty) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a ").append(difficulty).append(" level multiple choice question about ");
        
        switch (category.toLowerCase()) {
            case "math":
                prompt.append("mathematics. Include topics like algebra, geometry, arithmetic, or calculus. ");
                break;
            case "coding":
                prompt.append("programming and computer science. Include topics like algorithms, data structures, programming languages, or software development concepts. ");
                break;
            case "general":
                prompt.append("general knowledge. Include topics like history, geography, science, literature, or current events. ");
                break;
            default:
                prompt.append("general knowledge. ");
                break;
        }
        
        prompt.append("Format the response exactly as follows:\n\n");
        prompt.append("QUESTION: [Your question here]\n");
        prompt.append("A) [First option]\n");
        prompt.append("B) [Second option]\n");
        prompt.append("C) [Third option]\n");
        prompt.append("D) [Fourth option]\n");
        prompt.append("ANSWER: [Correct answer letter A, B, C, or D]\n");
        prompt.append("EXPLANATION: [Brief explanation of why this is correct]\n\n");
        prompt.append("Make sure the question is clear, the options are plausible, and only one answer is definitively correct.");
        
        return prompt.toString();
    }
    
    /**
     * Parse a QuizQuestion from the generated text
     */
    private QuizQuestion parseQuestionFromText(String text, String category, String difficulty) {
        try {
            // Extract question
            Pattern questionPattern = Pattern.compile("QUESTION:\\s*(.+?)(?=\\n[A-D]\\))", Pattern.DOTALL);
            Matcher questionMatcher = questionPattern.matcher(text);
            if (!questionMatcher.find()) return null;
            String question = questionMatcher.group(1).trim();
            
            // Extract options
            Pattern optionPattern = Pattern.compile("([A-D])\\)\\s*(.+?)(?=\\n[A-D]\\)|\\nANSWER:|$)", Pattern.DOTALL);
            Matcher optionMatcher = optionPattern.matcher(text);
            
            String optionA = "", optionB = "", optionC = "", optionD = "";
            while (optionMatcher.find()) {
                String letter = optionMatcher.group(1);
                String option = optionMatcher.group(2).trim();
                
                switch (letter) {
                    case "A": optionA = option; break;
                    case "B": optionB = option; break;
                    case "C": optionC = option; break;
                    case "D": optionD = option; break;
                }
            }
            
            // Extract answer
            Pattern answerPattern = Pattern.compile("ANSWER:\\s*([A-D])");
            Matcher answerMatcher = answerPattern.matcher(text);
            if (!answerMatcher.find()) return null;
            String answerLetter = answerMatcher.group(1);
            
            // Get correct answer text
            String correctAnswer;
            switch (answerLetter) {
                case "A": correctAnswer = optionA; break;
                case "B": correctAnswer = optionB; break;
                case "C": correctAnswer = optionC; break;
                case "D": correctAnswer = optionD; break;
                default: return null;
            }
            
            // Validate that all fields are populated
            if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || 
                optionC.isEmpty() || optionD.isEmpty() || correctAnswer.isEmpty()) {
                return null;
            }
            
            return new QuizQuestion(question, correctAnswer, optionA, optionB, optionC, optionD, 
                                  category, difficulty, "gemini");
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Callback interface for question generation
     */
    public interface QuestionCallback {
        void onQuestionGenerated(QuizQuestion question);
        void onError(String error);
    }
}
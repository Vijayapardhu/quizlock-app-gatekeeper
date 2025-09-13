package com.quizlock.gatekeeper.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Retrofit interface for Gemini API integration
 */
public interface GeminiApiService {
    
    @POST("v1beta/models/gemini-pro:generateContent")
    Call<GeminiResponse> generateContent(
        @Header("x-goog-api-key") String apiKey,
        @Body GeminiRequest request
    );
    
    /**
     * Request model for Gemini API
     */
    class GeminiRequest {
        private Contents[] contents;
        private GenerationConfig generationConfig;
        
        public GeminiRequest(String prompt, String category, String difficulty) {
            this.contents = new Contents[]{new Contents(prompt)};
            this.generationConfig = new GenerationConfig();
        }
        
        public static class Contents {
            private Parts[] parts;
            
            public Contents(String text) {
                this.parts = new Parts[]{new Parts(text)};
            }
        }
        
        public static class Parts {
            private String text;
            
            public Parts(String text) {
                this.text = text;
            }
        }
        
        public static class GenerationConfig {
            private double temperature = 0.7;
            private int maxOutputTokens = 500;
            private int topK = 40;
            private double topP = 0.8;
        }
    }
    
    /**
     * Response model for Gemini API
     */
    class GeminiResponse {
        private Candidate[] candidates;
        
        public Candidate[] getCandidates() {
            return candidates;
        }
        
        public static class Candidate {
            private Content content;
            
            public Content getContent() {
                return content;
            }
        }
        
        public static class Content {
            private Part[] parts;
            
            public Part[] getParts() {
                return parts;
            }
        }
        
        public static class Part {
            private String text;
            
            public String getText() {
                return text;
            }
        }
        
        /**
         * Extract the generated text from the response
         */
        public String getGeneratedText() {
            if (candidates != null && candidates.length > 0) {
                Content content = candidates[0].getContent();
                if (content != null && content.getParts() != null && content.getParts().length > 0) {
                    return content.getParts()[0].getText();
                }
            }
            return null;
        }
    }
}
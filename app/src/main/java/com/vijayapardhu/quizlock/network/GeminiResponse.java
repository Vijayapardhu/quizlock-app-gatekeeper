package com.vijayapardhu.quizlock.network;

import com.google.gson.annotations.SerializedName;

public class GeminiResponse {
    @SerializedName("candidates")
    private Candidate[] candidates;
    
    @SerializedName("usageMetadata")
    private UsageMetadata usageMetadata;
    
    public Candidate[] getCandidates() {
        return candidates;
    }
    
    public String getGeneratedText() {
        if (candidates != null && candidates.length > 0 && 
            candidates[0].content != null && candidates[0].content.parts != null && 
            candidates[0].content.parts.length > 0) {
            return candidates[0].content.parts[0].text;
        }
        return null;
    }
    
    public static class Candidate {
        @SerializedName("content")
        private Content content;
        
        @SerializedName("finishReason")
        private String finishReason;
        
        @SerializedName("index")
        private int index;
        
        @SerializedName("safetyRatings")
        private SafetyRating[] safetyRatings;
        
        public static class Content {
            @SerializedName("parts")
            private Part[] parts;
            
            @SerializedName("role")
            private String role;
            
            public static class Part {
                @SerializedName("text")
                private String text;
            }
        }
        
        public static class SafetyRating {
            @SerializedName("category")
            private String category;
            
            @SerializedName("probability")
            private String probability;
        }
    }
    
    public static class UsageMetadata {
        @SerializedName("promptTokenCount")
        private int promptTokenCount;
        
        @SerializedName("candidatesTokenCount")
        private int candidatesTokenCount;
        
        @SerializedName("totalTokenCount")
        private int totalTokenCount;
    }
}
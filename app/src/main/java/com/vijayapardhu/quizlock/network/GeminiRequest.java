package com.vijayapardhu.quizlock.network;

import com.google.gson.annotations.SerializedName;

public class GeminiRequest {
    @SerializedName("contents")
    private Content[] contents;
    
    @SerializedName("generationConfig")
    private GenerationConfig generationConfig;
    
    public GeminiRequest(String prompt) {
        this.contents = new Content[] { new Content(prompt) };
        this.generationConfig = new GenerationConfig();
    }
    
    public static class Content {
        @SerializedName("parts")
        private Part[] parts;
        
        public Content(String text) {
            this.parts = new Part[] { new Part(text) };
        }
        
        public static class Part {
            @SerializedName("text")
            private String text;
            
            public Part(String text) {
                this.text = text;
            }
        }
    }
    
    public static class GenerationConfig {
        @SerializedName("temperature")
        private float temperature = 0.7f;
        
        @SerializedName("topK")
        private int topK = 1;
        
        @SerializedName("topP")
        private float topP = 0.8f;
        
        @SerializedName("maxOutputTokens")
        private int maxOutputTokens = 2048;
        
        @SerializedName("stopSequences")
        private String[] stopSequences = {};
    }
}
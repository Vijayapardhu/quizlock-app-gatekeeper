package com.vijayapardhu.quizlock.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {
    
    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent")
    Call<GeminiResponse> generateContent(
        @Query("key") String apiKey,
        @Body GeminiRequest request
    );
}
package com.quizlock.gatekeeper.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Factory for creating Retrofit API services
 */
public class ApiClient {
    
    private static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/";
    private static Retrofit retrofit;
    private static GeminiApiService geminiService;
    
    /**
     * Get Retrofit instance
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // Create OkHttp client with logging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
            
            retrofit = new Retrofit.Builder()
                .baseUrl(GEMINI_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
    
    /**
     * Get Gemini API service instance
     */
    public static GeminiApiService getGeminiService() {
        if (geminiService == null) {
            geminiService = getRetrofit().create(GeminiApiService.class);
        }
        return geminiService;
    }
}
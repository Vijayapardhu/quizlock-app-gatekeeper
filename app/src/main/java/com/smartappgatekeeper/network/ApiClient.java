package com.smartappgatekeeper.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

/**
 * API Client for network requests
 * Configures Retrofit with OkHttp for API communication
 */
public class ApiClient {
    
    private static ApiClient instance;
    private Retrofit retrofit;
    private ApiService apiService;
    
    private ApiClient() {
        setupRetrofit();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }
    
    /**
     * Setup Retrofit with OkHttp client
     */
    private void setupRetrofit() {
        // Create logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        // Create OkHttp client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
        
        // Create Retrofit instance
        retrofit = new Retrofit.Builder()
            .baseUrl(com.smartappgatekeeper.utils.Constants.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        
        // Create API service
        apiService = retrofit.create(ApiService.class);
    }
    
    /**
     * Get API service instance
     */
    public ApiService getApiService() {
        return apiService;
    }
    
    /**
     * Get Retrofit instance
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }
}

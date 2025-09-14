package com.smartappgatekeeper.service;

import android.content.Context;
import android.util.Log;
import com.smartappgatekeeper.network.SupabaseClient;
import com.smartappgatekeeper.database.entities.*;
import com.smartappgatekeeper.model.Question;
import com.smartappgatekeeper.config.SupabaseConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Supabase Database Service
 * Handles data synchronization between local Room database and Supabase
 */
public class SupabaseDatabaseService {
    
    private static final String TAG = "SupabaseDatabaseService";
    private static SupabaseDatabaseService instance;
    private SupabaseClient supabaseClient;
    private ExecutorService executor;
    
    private SupabaseDatabaseService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
        this.executor = Executors.newCachedThreadPool();
    }
    
    public static synchronized SupabaseDatabaseService getInstance(Context context) {
        if (instance == null) {
            instance = new SupabaseDatabaseService(context);
        }
        return instance;
    }
    
    /**
     * Sync user profile to Supabase
     */
    public CompletableFuture<Boolean> syncUserProfile(UserProfile userProfile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = supabaseClient.getPostgrestUrl() + "/" + SupabaseConfig.TABLE_USER_PROFILES;
                JSONObject requestBody = new JSONObject();
                requestBody.put("id", userProfile.getId());
                requestBody.put("name", userProfile.getName());
                requestBody.put("email", userProfile.getEmail());
                requestBody.put("age", userProfile.getAge());
                requestBody.put("created_at", userProfile.getCreatedAt());
                requestBody.put("updated_at", userProfile.getLastUpdated());
                
                HttpURLConnection connection = createConnection(url, "POST");
                connection.setRequestProperty("apikey", supabaseClient.getAnonKey());
                connection.setRequestProperty("Authorization", "Bearer " + supabaseClient.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Prefer", "resolution=merge-duplicates");
                
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                }
                
                int responseCode = connection.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    Log.i(TAG, "User profile synced successfully");
                    return true;
                } else {
                    String response = readResponse(connection);
                    Log.e(TAG, "Failed to sync user profile: " + response);
                    return false;
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error syncing user profile: " + e.getMessage());
                return false;
            }
        }, executor);
    }
    
    /**
     * Sync target apps to Supabase
     */
    public CompletableFuture<Boolean> syncTargetApps(List<TargetApp> targetApps) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = supabaseClient.getPostgrestUrl() + "/" + SupabaseConfig.TABLE_TARGET_APPS;
                JSONArray requestBody = new JSONArray();
                
                for (TargetApp app : targetApps) {
                    JSONObject appJson = new JSONObject();
                    appJson.put("id", app.getId());
                    appJson.put("package_name", app.getPackageName());
                    appJson.put("app_name", app.getAppName());
                    appJson.put("category", app.getCategory());
                    appJson.put("is_enabled", app.isEnabled());
                    appJson.put("created_at", app.getCreatedAt());
                    appJson.put("updated_at", app.getLastUpdated());
                    requestBody.put(appJson);
                }
                
                HttpURLConnection connection = createConnection(url, "POST");
                connection.setRequestProperty("apikey", supabaseClient.getAnonKey());
                connection.setRequestProperty("Authorization", "Bearer " + supabaseClient.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Prefer", "resolution=merge-duplicates");
                
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                }
                
                int responseCode = connection.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    Log.i(TAG, "Target apps synced successfully");
                    return true;
                } else {
                    String response = readResponse(connection);
                    Log.e(TAG, "Failed to sync target apps: " + response);
                    return false;
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error syncing target apps: " + e.getMessage());
                return false;
            }
        }, executor);
    }
    
    /**
     * Sync questions to Supabase
     */
    // Commented out for simplified implementation
    /*
    public CompletableFuture<Boolean> syncQuestions(List<Question> questions) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = supabaseClient.getPostgrestUrl() + "/" + SupabaseConfig.TABLE_QUESTIONS;
                JSONArray requestBody = new JSONArray();
                
                for (Question question : questions) {
                    JSONObject questionJson = new JSONObject();
                    questionJson.put("id", question.getId());
                    questionJson.put("question_text", question.getQuestionText());
                    questionJson.put("option_a", question.getOptionA());
                    questionJson.put("option_b", question.getOptionB());
                    questionJson.put("option_c", question.getOptionC());
                    questionJson.put("option_d", question.getOptionD());
                    questionJson.put("correct_answer", question.getCorrectAnswer());
                    questionJson.put("explanation", question.getExplanation());
                    questionJson.put("topic", question.getTopic());
                    questionJson.put("difficulty", question.getDifficulty());
                    questionJson.put("source", question.getSource());
                    questionJson.put("ai_model", question.getAiModel());
                    questionJson.put("time_limit_seconds", question.getTimeLimitSeconds());
                    questionJson.put("is_active", question.isActive());
                    questionJson.put("created_at", question.getCreatedAt());
                    questionJson.put("updated_at", question.getLastUpdated());
                    requestBody.put(questionJson);
                }
                
                HttpURLConnection connection = createConnection(url, "POST");
                connection.setRequestProperty("apikey", supabaseClient.getAnonKey());
                connection.setRequestProperty("Authorization", "Bearer " + supabaseClient.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Prefer", "resolution=merge-duplicates");
                
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                }
                
                int responseCode = connection.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    Log.i(TAG, "Questions synced successfully");
                    return true;
                } else {
                    String response = readResponse(connection);
                    Log.e(TAG, "Failed to sync questions: " + response);
                    return false;
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error syncing questions: " + e.getMessage());
                return false;
            }
        }, executor);
    }
    */
    
    /**
     * Sync usage events to Supabase
     */
    public CompletableFuture<Boolean> syncUsageEvents(List<UsageEvent> usageEvents) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = supabaseClient.getPostgrestUrl() + "/" + SupabaseConfig.TABLE_USAGE_EVENTS;
                JSONArray requestBody = new JSONArray();
                
                for (UsageEvent event : usageEvents) {
                    JSONObject eventJson = new JSONObject();
                    eventJson.put("id", event.getId());
                    eventJson.put("user_id", "current_user"); // Will be replaced by Supabase RLS
                    eventJson.put("package_name", event.getPackageName());
                    eventJson.put("event_type", event.getEventType());
                    eventJson.put("timestamp", event.getTimestamp());
                    eventJson.put("duration_seconds", event.getDurationSeconds());
                    eventJson.put("quiz_attempted", event.getQuestionId() > 0);
                    eventJson.put("quiz_passed", event.isSuccess());
                    eventJson.put("created_at", event.getTimestamp());
                    requestBody.put(eventJson);
                }
                
                HttpURLConnection connection = createConnection(url, "POST");
                connection.setRequestProperty("apikey", supabaseClient.getAnonKey());
                connection.setRequestProperty("Authorization", "Bearer " + supabaseClient.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Prefer", "resolution=merge-duplicates");
                
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                }
                
                int responseCode = connection.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    Log.i(TAG, "Usage events synced successfully");
                    return true;
                } else {
                    String response = readResponse(connection);
                    Log.e(TAG, "Failed to sync usage events: " + response);
                    return false;
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error syncing usage events: " + e.getMessage());
                return false;
            }
        }, executor);
    }
    
    /**
     * Create HTTP connection
     */
    private HttpURLConnection createConnection(String urlString, String method) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection;
    }
    
    /**
     * Read response from connection
     */
    private String readResponse(HttpURLConnection connection) throws Exception {
        BufferedReader reader;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}

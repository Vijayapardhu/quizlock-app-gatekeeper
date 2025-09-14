package com.smartappgatekeeper.service;

import android.content.Context;
import android.util.Log;
import com.smartappgatekeeper.network.SupabaseClient;
import com.smartappgatekeeper.database.entities.UserProfile;
import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Supabase Authentication Service
 * Handles user authentication, registration, and profile management
 */
public class SupabaseAuthService {
    
    private static final String TAG = "SupabaseAuthService";
    private static SupabaseAuthService instance;
    private SupabaseClient supabaseClient;
    private ExecutorService executor;
    private String currentUserId;
    private String currentUserEmail;
    private String currentAccessToken;
    
    private SupabaseAuthService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
        this.executor = Executors.newCachedThreadPool();
    }
    
    public static synchronized SupabaseAuthService getInstance(Context context) {
        if (instance == null) {
            instance = new SupabaseAuthService(context);
        }
        return instance;
    }
    
    /**
     * Register a new user with email and password
     */
    public CompletableFuture<AuthResult> registerUser(String email, String password, String fullName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String authUrl = supabaseClient.getAuthUrl() + "/signup";
                JSONObject requestBody = new JSONObject();
                requestBody.put("email", email);
                requestBody.put("password", password);
                requestBody.put("data", new JSONObject().put("full_name", fullName));
                
                HttpURLConnection connection = createConnection(authUrl, "POST");
                connection.setRequestProperty("apikey", supabaseClient.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                
                // Send request
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                }
                
                int responseCode = connection.getResponseCode();
                String response = readResponse(connection);
                
                if (responseCode == 200 || responseCode == 201) {
                    JSONObject responseJson = new JSONObject(response);
                    currentUserId = responseJson.optString("user", "");
                    currentUserEmail = email;
                    currentAccessToken = responseJson.optString("access_token", "");
                    
                    Log.i(TAG, "User registered successfully: " + email);
                    return new AuthResult(true, "Registration successful", currentAccessToken, currentUserId);
                } else {
                    Log.e(TAG, "Registration failed: " + response);
                    return new AuthResult(false, "Registration failed: " + response, null, null);
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Registration error: " + e.getMessage());
                return new AuthResult(false, "Registration error: " + e.getMessage(), null, null);
            }
        }, executor);
    }
    
    /**
     * Sign in user with email and password
     */
    public CompletableFuture<AuthResult> signInUser(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String authUrl = supabaseClient.getAuthUrl() + "/token?grant_type=password";
                JSONObject requestBody = new JSONObject();
                requestBody.put("email", email);
                requestBody.put("password", password);
                
                HttpURLConnection connection = createConnection(authUrl, "POST");
                connection.setRequestProperty("apikey", supabaseClient.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                
                // Send request
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                }
                
                int responseCode = connection.getResponseCode();
                String response = readResponse(connection);
                
                if (responseCode == 200) {
                    JSONObject responseJson = new JSONObject(response);
                    currentUserId = responseJson.optString("user", "");
                    currentUserEmail = email;
                    currentAccessToken = responseJson.optString("access_token", "");
                    
                    Log.i(TAG, "User signed in successfully: " + email);
                    return new AuthResult(true, "Sign in successful", currentAccessToken, currentUserId);
                } else {
                    Log.e(TAG, "Sign in failed: " + response);
                    return new AuthResult(false, "Sign in failed: " + response, null, null);
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Sign in error: " + e.getMessage());
                return new AuthResult(false, "Sign in error: " + e.getMessage(), null, null);
            }
        }, executor);
    }
    
    /**
     * Sign out current user
     */
    public CompletableFuture<Boolean> signOut() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                currentUserId = null;
                currentUserEmail = null;
                currentAccessToken = null;
                Log.i(TAG, "User signed out successfully");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Sign out error: " + e.getMessage());
                return false;
            }
        }, executor);
    }
    
    /**
     * Get current user ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }
    
    /**
     * Get current user email
     */
    public String getCurrentUserEmail() {
        return currentUserEmail;
    }
    
    /**
     * Get current access token
     */
    public String getCurrentAccessToken() {
        return currentAccessToken;
    }
    
    /**
     * Check if user is signed in
     */
    public boolean isSignedIn() {
        return currentUserId != null && currentAccessToken != null;
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
    
    /**
     * Auth result class
     */
    public static class AuthResult {
        private final boolean success;
        private final String message;
        private final String accessToken;
        private final String userId;
        
        public AuthResult(boolean success, String message, String accessToken, String userId) {
            this.success = success;
            this.message = message;
            this.accessToken = accessToken;
            this.userId = userId;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getAccessToken() { return accessToken; }
        public String getUserId() { return userId; }
    }
}

package com.smartappgatekeeper.utils;

import android.util.Log;
import com.smartappgatekeeper.config.AppConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Supabase Connection Test Utility
 * Tests the Supabase connection without requiring UI
 */
public class SupabaseConnectionTest {
    
    private static final String TAG = "SupabaseConnectionTest";
    
    /**
     * Test basic Supabase connection
     */
    public static void testConnection() {
        Log.i(TAG, "Starting Supabase connection test...");
        
        // Test 1: Check configuration
        Log.i(TAG, "Configuration Test:");
        Log.i(TAG, "URL: " + AppConfig.SUPABASE_URL);
        Log.i(TAG, "Anon Key: " + AppConfig.SUPABASE_ANON_KEY.substring(0, 20) + "...");
        Log.i(TAG, "Service Role Key: " + AppConfig.SUPABASE_SERVICE_ROLE_KEY.substring(0, 20) + "...");
        
        // Test 2: Test HTTP connection
        testHttpConnection();
    }
    
    /**
     * Test HTTP connection to Supabase
     */
    private static void testHttpConnection() {
        new Thread(() -> {
            try {
                Log.i(TAG, "Testing HTTP connection...");
                
                String postgrestUrl = AppConfig.SUPABASE_URL + "/rest/v1";
                URL url = new URL(postgrestUrl + "/user_courses");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", AppConfig.SUPABASE_ANON_KEY);
                connection.setRequestProperty("Authorization", "Bearer " + AppConfig.SUPABASE_ANON_KEY);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    Log.i(TAG, "✅ HTTP connection successful (200)");
                } else if (responseCode == 401) {
                    Log.i(TAG, "⚠️ HTTP connection successful but unauthorized (401) - This is expected if RLS is enabled");
                } else {
                    Log.w(TAG, "❌ HTTP connection failed (" + responseCode + ")");
                    
                    // Read error response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        error.append(line);
                    }
                    reader.close();
                    Log.w(TAG, "Error response: " + error.toString());
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                Log.e(TAG, "❌ HTTP connection error: " + e.getMessage(), e);
            }
        }).start();
    }
    
    /**
     * Test authentication endpoint
     */
    public static void testAuthentication() {
        new Thread(() -> {
            try {
                Log.i(TAG, "Testing authentication endpoint...");
                
                String authUrl = AppConfig.SUPABASE_URL + "/auth/v1";
                URL url = new URL(authUrl + "/settings");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", AppConfig.SUPABASE_ANON_KEY);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    Log.i(TAG, "✅ Authentication endpoint accessible (200)");
                } else {
                    Log.w(TAG, "❌ Authentication endpoint failed (" + responseCode + ")");
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                Log.e(TAG, "❌ Authentication test error: " + e.getMessage(), e);
            }
        }).start();
    }
    
    /**
     * Test database query
     */
    public static void testDatabaseQuery() {
        new Thread(() -> {
            try {
                Log.i(TAG, "Testing database query...");
                
                String postgrestUrl = AppConfig.SUPABASE_URL + "/rest/v1";
                URL url = new URL(postgrestUrl + "/user_courses?select=*&limit=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", AppConfig.SUPABASE_ANON_KEY);
                connection.setRequestProperty("Authorization", "Bearer " + AppConfig.SUPABASE_ANON_KEY);
                connection.setRequestProperty("Content-Type", "application/json");
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    Log.i(TAG, "✅ Database query successful");
                    Log.i(TAG, "Response: " + response.toString().substring(0, Math.min(100, response.length())) + "...");
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        error.append(line);
                    }
                    reader.close();
                    
                    Log.w(TAG, "❌ Database query failed (" + responseCode + ")");
                    Log.w(TAG, "Error: " + error.toString());
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                Log.e(TAG, "❌ Database test error: " + e.getMessage(), e);
            }
        }).start();
    }
}
